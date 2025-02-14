package com.youyi.domain.ugc.repository;

import com.youyi.common.exception.AppSystemException;
import com.youyi.common.type.InfraCode;
import com.youyi.common.type.InfraType;
import com.youyi.domain.ugc.repository.mapper.UgcTagMapper;
import com.youyi.domain.ugc.repository.po.UgcTagPO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static com.youyi.common.constant.RepositoryConstant.SINGLE_DML_AFFECTED_ROWS;
import static com.youyi.common.util.LogUtil.infraLog;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/25
 */
@Repository
@RequiredArgsConstructor
public class UgcTagRepository {

    private static final Logger logger = LoggerFactory.getLogger(UgcTagRepository.class);

    private final UgcTagMapper ugcTagMapper;

    public void insertTag(UgcTagPO po) {
        try {
            checkNotNull(po);
            int ret = ugcTagMapper.insert(po);
            checkState(ret == SINGLE_DML_AFFECTED_ROWS);
        } catch (Exception e) {
            infraLog(logger, InfraType.MYSQL, InfraCode.MYSQL_ERROR, e);
            throw AppSystemException.of(InfraCode.MYSQL_ERROR, e);
        }
    }

    public void insertBatchTag(List<UgcTagPO> poList) {
        try {
            checkState(CollectionUtils.isNotEmpty(poList));
            int ret = ugcTagMapper.insertBatch(poList);
            checkState(ret == poList.size());
        } catch (Exception e) {
            infraLog(logger, InfraType.MYSQL, InfraCode.MYSQL_ERROR, e);
            throw AppSystemException.of(InfraCode.MYSQL_ERROR, e);
        }
    }

    public List<UgcTagPO> queryByType(Integer type) {
        try {
            checkNotNull(type);
            return ugcTagMapper.queryByType(type);
        } catch (Exception e) {
            infraLog(logger, InfraType.MYSQL, InfraCode.MYSQL_ERROR, e);
            throw AppSystemException.of(InfraCode.MYSQL_ERROR, e);
        }
    }

    public List<UgcTagPO> queryAll() {
        try {
            return ugcTagMapper.queryAll();
        } catch (Exception e) {
            infraLog(logger, InfraType.MYSQL, InfraCode.MYSQL_ERROR, e);
            throw AppSystemException.of(InfraCode.MYSQL_ERROR, e);
        }
    }

    public List<UgcTagPO> queryByCursor(String cursor, int size) {
        try {
            checkState(StringUtils.isNotBlank(cursor) && size > 0);
            return ugcTagMapper.queryByCursor(cursor, size);
        } catch (Exception e) {
            infraLog(logger, InfraType.MYSQL, InfraCode.MYSQL_ERROR, e);
            throw AppSystemException.of(InfraCode.MYSQL_ERROR, e);
        }
    }
}
