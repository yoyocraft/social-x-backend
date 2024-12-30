package com.youyi.core.config.repository;

import com.youyi.common.exception.AppBizException;
import com.youyi.common.type.InfraCode;
import com.youyi.common.type.InfraType;
import com.youyi.core.config.repository.mapper.ConfigMapper;
import com.youyi.core.config.repository.po.ConfigPO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import static com.google.common.base.Preconditions.checkState;
import static com.youyi.common.constant.RepositoryConstant.SINGLE_INSERT_AFFECTED_ROWS;
import static com.youyi.common.util.LogUtil.infraLog;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2024/12/29
 */
@Repository
@RequiredArgsConstructor
public class ConfigRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigRepository.class);

    private final ConfigMapper configMapper;

    public void insert(ConfigPO po) {
        try {
            int ret = configMapper.insert(po);
            checkState(ret == SINGLE_INSERT_AFFECTED_ROWS);
        } catch (Exception e) {
            infraLog(LOGGER, InfraType.MYSQL, InfraCode.MYSQL_ERROR, e);
            throw AppBizException.of(InfraCode.MYSQL_ERROR, e);
        }
    }

}
