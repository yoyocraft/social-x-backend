package com.youyi.domain.ugc.core;

import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import com.youyi.common.constant.SymbolConstant;
import com.youyi.common.constant.UgcConstant;
import com.youyi.common.type.conf.ConfigKey;
import com.youyi.common.type.ugc.UgcStatus;
import com.youyi.common.type.ugc.UgcType;
import com.youyi.common.util.MediaUtil;
import com.youyi.domain.media.core.LocalImageManager;
import com.youyi.domain.media.model.MediaResourceDO;
import com.youyi.domain.ugc.model.UgcExtraData;
import com.youyi.domain.ugc.repository.UgcRepository;
import com.youyi.domain.ugc.repository.document.UgcDocument;
import com.youyi.infra.detect.ImageDetectClient;
import com.youyi.infra.detect.model.ImageDetectRequest;
import com.youyi.infra.detect.model.ImageDetectResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.youyi.common.constant.ErrorCodeConstant.SUCCESS_CODE;
import static com.youyi.common.constant.MediaConstant.IMAGE_NORMAL_CLASSIFY;
import static com.youyi.common.constant.MediaConstant.IMAGE_PORN_CLASSIFY;
import static com.youyi.common.constant.MediaConstant.IMAGE_SENSITIVE_CLASSIFY;
import static com.youyi.common.util.ext.MoreFeatures.runWithCost;
import static com.youyi.domain.ugc.util.AuditUtil.checkSensitiveContent;
import static com.youyi.infra.conf.core.Conf.getBooleanConfig;
import static com.youyi.infra.conf.core.Conf.getIntegerConfig;
import static com.youyi.infra.conf.core.Conf.getStringConfig;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/24
 */
@Component
@RequiredArgsConstructor
public class UgcAuditJob {

    private static final Logger logger = LoggerFactory.getLogger(UgcAuditJob.class);

    private static final long AUDIT_UGC_INTERVAL = 30000L;
    private final UgcTpeContainer ugcTpeContainer;

    private final UgcRepository ugcRepository;

    private final ImageDetectClient imageDetectClient;
    private final LocalImageManager localImageManager;

    @Scheduled(initialDelay = AUDIT_UGC_INTERVAL, fixedDelay = AUDIT_UGC_INTERVAL)
    public void auditUgcJob() {
        try {
            runWithCost(logger, this::auditUgc, "auditUgc");
        } catch (Exception e) {
            logger.error("[UgcAuditJob] audit ugc exp", e);
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
            UgcType.ALL.name(),
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
        if (CollectionUtils.isNotEmpty(ugcDocument.getTags())) {
            isAuditReject |= ugcDocument.getTags().stream()
                .filter(SensitiveWordHelper::contains)
                .peek(tag -> auditRetBuilder.append("标签包含敏感词：").append(tag).append(SymbolConstant.NEW_LINE))
                .findAny()
                .isPresent();
        }
        // 检查图片是否敏感
        boolean detectImageAB = getBooleanConfig(ConfigKey.AUDIT_IMAGE_AB_SWITCH);
        if (detectImageAB && CollectionUtils.isNotEmpty(ugcDocument.getAttachmentUrls())) {
            isAuditReject |= checkSensitiveImage(ugcDocument.getAttachmentUrls(), auditRetBuilder);
        }

        // 根据检测结果设置状态
        UgcExtraData extraData = Optional.ofNullable(ugcDocument.getExtraData()).orElseGet(UgcExtraData::new);
        ugcDocument.setExtraData(extraData);
        if (isAuditReject) {
            ugcDocument.setStatus(UgcStatus.REJECTED.name());
            extraData.setAuditRet(auditRetBuilder.toString());
        } else {
            ugcDocument.setStatus(UgcStatus.PUBLISHED.name());
            extraData.setAuditRet(UgcConstant.AUDIT_PASS);
        }

        ugcDocument.setGmtModified(System.currentTimeMillis());
        ugcRepository.updateUgc(ugcDocument);
    }

    private boolean checkSensitiveImage(List<String> attachmentUrls, StringBuilder auditRetBuilder) {
        List<ImageDetectRequest.ImageInfo> imageInfos = getImageInfos(attachmentUrls);
        if (CollectionUtils.isEmpty(imageInfos)) {
            return false;
        }

        ImageDetectResponse response = sendImageDetectRequest(imageInfos);
        if (!SUCCESS_CODE.equals(response.getCode()) || CollectionUtils.isEmpty(response.getData())) {
            return false;
        }

        return processDetectResults(response.getData(), auditRetBuilder);
    }

    private List<ImageDetectRequest.ImageInfo> getImageInfos(List<String> attachmentUrls) {
        List<ImageDetectRequest.ImageInfo> imageInfos = new ArrayList<>();
        for (String url : attachmentUrls) {
            try {
                MediaResourceDO resourceDO = localImageManager.getMediaResourceByUrl(url);
                if (resourceDO != null) {
                    imageInfos.add(new ImageDetectRequest.ImageInfo(
                        resourceDO.getResourceUrl(),
                        MediaUtil.encodeImageToBase64FromPath(resourceDO.getMedia())
                    ));
                }
            } catch (Exception e) {
                logger.warn("Failed to process image URL: {}, error: {}", url, e.getMessage());
            }
        }
        return imageInfos;
    }

    private ImageDetectResponse sendImageDetectRequest(List<ImageDetectRequest.ImageInfo> imageInfos) {
        ImageDetectRequest request = new ImageDetectRequest();
        request.setImages(imageInfos);
        return imageDetectClient.checkImage(request);
    }

    private boolean processDetectResults(List<ImageDetectResponse.DetectResult> detectResults, StringBuilder auditRetBuilder) {
        boolean isAuditReject = false;
        double threshold = Double.parseDouble(getStringConfig(ConfigKey.PROB_THRESHOLD));

        for (ImageDetectResponse.DetectResult detectResult : detectResults) {
            isAuditReject |= processSensitiveDetectResult(detectResult.getSensitiveDetectResult(), auditRetBuilder);

            for (ImageDetectResponse.ImageClassifyInfo info : detectResult.getImageClassifyResult()) {
                isAuditReject |= processImageClassifyInfo(info, threshold, auditRetBuilder);
            }
        }

        return isAuditReject;
    }

    private boolean processSensitiveDetectResult(ImageDetectResponse.SensitiveDetectResult result, StringBuilder auditRetBuilder) {
        if (result != null && result.getKeyword()) {
            auditRetBuilder.append("图片包含敏感词").append(SymbolConstant.NEW_LINE);
            return true;
        }
        return false;
    }

    private boolean processImageClassifyInfo(ImageDetectResponse.ImageClassifyInfo info, double threshold, StringBuilder auditRetBuilder) {
        if (IMAGE_NORMAL_CLASSIFY.equals(info.getLabel()) && info.getProb() >= threshold) {
            return false;
        }

        if (IMAGE_SENSITIVE_CLASSIFY.equals(info.getLabel()) && info.getProb() >= threshold) {
            auditRetBuilder.append("图片包含敏感内容").append(SymbolConstant.NEW_LINE);
            return true;
        }

        if (IMAGE_PORN_CLASSIFY.equals(info.getLabel()) && info.getProb() >= threshold) {
            auditRetBuilder.append("图片包含色情内容").append(SymbolConstant.NEW_LINE);
            return true;
        }

        return false;
    }
}
