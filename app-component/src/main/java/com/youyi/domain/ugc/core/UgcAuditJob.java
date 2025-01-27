package com.youyi.domain.ugc.core;

import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import com.youyi.common.constant.SymbolConstant;
import com.youyi.common.type.conf.ConfigKey;
import com.youyi.common.type.ugc.UgcStatusType;
import com.youyi.common.util.GsonUtil;
import com.youyi.common.wrapper.ThreadPoolConfigWrapper;
import com.youyi.domain.ugc.model.UgcExtraData;
import com.youyi.domain.ugc.repository.UgcRepository;
import com.youyi.domain.ugc.repository.document.UgcDocument;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import javax.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.youyi.common.type.conf.ConfigKey.AUDIT_UGC_THREAD_POOL_CONFIG;
import static com.youyi.common.util.ext.MoreFeatures.runWithCost;
import static com.youyi.infra.conf.core.SystemConfigService.checkConfig;
import static com.youyi.infra.conf.core.SystemConfigService.getCacheValue;
import static com.youyi.infra.conf.core.SystemConfigService.getIntegerConfig;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/24
 */
@Component
@RequiredArgsConstructor
public class UgcAuditJob implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UgcAuditJob.class);

    private static final long AUDIT_UGC_INTERVAL = 30000L;
    private static ThreadPoolExecutor auditUgcExecutor;

    private final UgcRepository ugcRepository;

    @Scheduled(fixedDelay = AUDIT_UGC_INTERVAL)
    public void auditUgcJob() {
        try {
            runWithCost(LOGGER, this::auditUgc, "auditUgc");
        } catch (Exception e) {
            LOGGER.error("[UgcAuditJob] audit ugc exp", e);
        }
    }

    @Override
    public void onApplicationEvent(@Nonnull ApplicationReadyEvent event) {
        checkConfig(AUDIT_UGC_THREAD_POOL_CONFIG);
        initAsyncExecutor();
    }

    public void auditUgc() {
        LocalDateTime cursor = LocalDateTime.now();
        while (true) {
            List<UgcDocument> ugcPage = loadToAuditUgc(cursor);
            if (ugcPage.isEmpty()) {
                break;
            }
            ugcPage.forEach(ugcDocument -> auditUgcExecutor.execute(() -> doAuditUgc(ugcDocument)));
            cursor = ugcPage.get(ugcPage.size() - 1).getGmtModified();
        }
    }

    public List<UgcDocument> loadToAuditUgc(LocalDateTime cursor) {
        return ugcRepository.queryByStatusWithTimeCursor(
            UgcStatusType.AUDITING.name(),
            cursor,
            getIntegerConfig(ConfigKey.DEFAULT_PAGE_SIZE)
        );
    }

    public void doAuditUgc(UgcDocument ugcDocument) {
        if (!UgcStatusType.AUDITING.equals(UgcStatusType.valueOf(ugcDocument.getStatus()))) {
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
            ugcDocument.setStatus(UgcStatusType.REJECTED.name());
            UgcExtraData extraData = new UgcExtraData();
            extraData.setAuditRet(auditRetBuilder.toString());
            ugcDocument.setExtraData(GsonUtil.toJson(extraData));
        } else {
            ugcDocument.setStatus(UgcStatusType.PUBLISHED.name());
        }

        ugcDocument.setGmtModified(LocalDateTime.now());
        ugcRepository.updateUgc(ugcDocument);

        // TODO youyi 2025/1/24 接入 AI 审核
    }

    /**
     * 检查字段内容是否包含敏感词
     */
    private boolean checkSensitiveContent(String fieldName, String content, StringBuilder auditRetBuilder) {
        if (StringUtils.isNotBlank(content) && SensitiveWordHelper.contains(content)) {
            auditRetBuilder.append(fieldName).append("包含敏感词；").append(SymbolConstant.NEW_LINE);
            return true;
        }
        return false;
    }

    private void initAsyncExecutor() {
        ThreadPoolConfigWrapper auditUgcTpeConfig = getCacheValue(AUDIT_UGC_THREAD_POOL_CONFIG, ThreadPoolConfigWrapper.class);
        auditUgcExecutor = new ThreadPoolExecutor(
            auditUgcTpeConfig.getCorePoolSize(),
            auditUgcTpeConfig.getMaximumPoolSize(),
            auditUgcTpeConfig.getKeepAliveTime(),
            auditUgcTpeConfig.getTimeUnit(),
            auditUgcTpeConfig.getQueue(),
            auditUgcTpeConfig.getThreadFactory(LOGGER),
            auditUgcTpeConfig.getRejectedHandler()
        );
    }
}
