package com.youyi.domain.ugc.processor;

import com.youyi.common.util.GsonUtil;
import com.youyi.domain.task.core.TaskProcessor;
import com.youyi.domain.task.model.SysTaskExtraData;
import com.youyi.domain.ugc.model.UgcExtraData;
import com.youyi.domain.ugc.repository.UgcRepository;
import com.youyi.domain.ugc.repository.document.UgcDocument;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/02/09
 */
@Component
@RequiredArgsConstructor
public class UgcAdoptProcessor implements TaskProcessor {

    private static final Logger logger = LoggerFactory.getLogger(UgcDeleteProcessor.class);

    private final UgcRepository ugcRepository;

    @Override
    public void process(String taskId, SysTaskExtraData extraData) {
        if (Objects.isNull(extraData)) {
            logger.warn("extraData is null, taskId: {}", taskId);
            return;
        }
        String ugcId = extraData.getTargetId();
        logger.info("[TaskProcessor]adopt ugc, taskId: {}, extraData: {}", taskId, GsonUtil.toJson(extraData));

        UgcDocument ugcDocument = ugcRepository.queryByUgcId(ugcId);
        if (Objects.isNull(ugcDocument)) {
            logger.warn("ugcDocument is null, taskId: {}, extraData: {}", taskId, GsonUtil.toJson(extraData));
            return;
        }
        UgcExtraData ugcExtraData = Optional.ofNullable(ugcDocument.getExtraData()).orElseGet(UgcExtraData::new);
        if (ugcExtraData.getHasSolved()) {
            return;
        }
        ugcExtraData.setHasSolved(Boolean.TRUE);
        ugcRepository.updateUgc(ugcDocument);
    }
}
