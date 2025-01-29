package com.youyi.domain.ugc.core;

import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import com.youyi.common.constant.SymbolConstant;
import com.youyi.common.type.conf.ConfigKey;
import com.youyi.common.type.ugc.UgcStatus;
import com.youyi.domain.ugc.model.UgcExtraData;
import com.youyi.domain.ugc.repository.UgcRepository;
import com.youyi.domain.ugc.repository.document.UgcDocument;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.youyi.common.util.ext.MoreFeatures.runWithCost;
import static com.youyi.domain.ugc.util.AuditUtil.checkSensitiveContent;
import static com.youyi.infra.conf.core.SystemConfigService.getIntegerConfig;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/24
 */
@Component
@RequiredArgsConstructor
public class UgcAuditJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(UgcAuditJob.class);

    private static final long AUDIT_UGC_INTERVAL = 30000L;
    private final UgcTpeContainer ugcTpeContainer;

    private final UgcRepository ugcRepository;

    @Scheduled(initialDelay = AUDIT_UGC_INTERVAL, fixedDelay = AUDIT_UGC_INTERVAL)
    public void auditUgcJob() {
        try {
            runWithCost(LOGGER, this::auditUgc, "auditUgc");
        } catch (Exception e) {
            LOGGER.error("[UgcAuditJob] audit ugc exp", e);
        }
    }

    public void auditUgc() {
        long cursor = System.currentTimeMillis();
        while (true) {
            List<UgcDocument> ugcList = loadAuditingUgc(cursor);
            if (ugcList.isEmpty()) {
                break;
            }
            ugcList.forEach(ugcDocument -> ugcTpeContainer.getAuditUgcExecutor().execute(() -> doAuditUgc(ugcDocument)));
            cursor = ugcList.get(ugcList.size() - 1).getGmtModified();
        }
    }

    private List<UgcDocument> loadAuditingUgc(long cursor) {
        return ugcRepository.queryByStatusWithTimeCursor(
            UgcStatus.AUDITING.name(),
            cursor,
            getIntegerConfig(ConfigKey.DEFAULT_PAGE_SIZE)
        );
    }

    public void doAuditUgc(UgcDocument ugcDocument) {
        if (!UgcStatus.AUDITING.equals(UgcStatus.of(ugcDocument.getStatus()))) {
            return;
        }

        boolean isAuditReject = false;
        StringBuilder auditRetBuilder = new StringBuilder();

        // 检查 UGC 的各个字段
        isAuditReject |= checkSensitiveContent("标题", ugcDocument.getTitle(), auditRetBuilder);
        isAuditReject |= checkSensitiveContent("内容", ugcDocument.getContent(), auditRetBuilder);
        isAuditReject |= checkSensitiveContent("摘要", ugcDocument.getSummary(), auditRetBuilder);

        // 检查标签中的敏感词
        isAuditReject |= ugcDocument.getTags().stream()
            .filter(SensitiveWordHelper::contains)
            .peek(tag -> auditRetBuilder.append("标签包含敏感词：").append(tag).append(SymbolConstant.NEW_LINE))
            .findAny()
            .isPresent();

        // 根据检测结果设置状态
        if (isAuditReject) {
            ugcDocument.setStatus(UgcStatus.REJECTED.name());
            UgcExtraData extraData = Optional.ofNullable(ugcDocument.getExtraData()).orElseGet(UgcExtraData::new);
            extraData.setAuditRet(auditRetBuilder.toString());
            ugcDocument.setExtraData(extraData);
        } else {
            ugcDocument.setStatus(UgcStatus.PUBLISHED.name());
        }

        ugcDocument.setGmtModified(System.currentTimeMillis());
        ugcRepository.updateUgc(ugcDocument);

        // TODO youyi 2025/1/24 接入 AI 审核
    }
}
