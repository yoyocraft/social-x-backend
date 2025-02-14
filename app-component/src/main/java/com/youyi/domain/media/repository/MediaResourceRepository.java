package com.youyi.domain.media.repository;

import com.youyi.common.exception.AppSystemException;
import com.youyi.common.type.InfraCode;
import com.youyi.common.type.InfraType;
import com.youyi.domain.media.repository.mapper.MediaResourceMapper;
import com.youyi.domain.media.repository.po.MediaResourcePO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static com.youyi.common.constant.RepositoryConstant.SINGLE_DML_AFFECTED_ROWS;
import static com.youyi.common.util.LogUtil.infraLog;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/17
 */
@Repository
@RequiredArgsConstructor
public class MediaResourceRepository {

    private static final Logger logger = LoggerFactory.getLogger(MediaResourceRepository.class);

    private final MediaResourceMapper mediaResourceMapper;

    public void insertMedia(MediaResourcePO mediaResourcePO) {
        try {
            checkNotNull(mediaResourcePO);
            int ret = mediaResourceMapper.insert(mediaResourcePO);
            checkState(ret == SINGLE_DML_AFFECTED_ROWS);
        } catch (Exception e) {
            infraLog(logger, InfraType.MYSQL, InfraCode.MYSQL_ERROR, e);
            throw AppSystemException.of(InfraCode.MYSQL_ERROR, e);
        }
    }
}
