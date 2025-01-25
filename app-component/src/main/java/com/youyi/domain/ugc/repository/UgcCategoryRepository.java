package com.youyi.domain.ugc.repository;

import com.youyi.common.exception.AppSystemException;
import com.youyi.common.type.InfraCode;
import com.youyi.common.type.InfraType;
import com.youyi.domain.ugc.repository.mapper.UgcCategoryMapper;
import com.youyi.domain.ugc.repository.po.UgcCategoryPO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
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
public class UgcCategoryRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(UgcCategoryRepository.class);

    private final UgcCategoryMapper ugcCategoryMapper;

    public void insertCategory(UgcCategoryPO po) {
        try {
            checkNotNull(po);
            int ret = ugcCategoryMapper.insert(po);
            checkState(ret == SINGLE_DML_AFFECTED_ROWS);
        } catch (Exception e) {
            infraLog(LOGGER, InfraType.MYSQL, InfraCode.MYSQL_ERROR, e);
            throw AppSystemException.of(InfraCode.MYSQL_ERROR, e);
        }
    }

    public void insertBatchCategory(List<UgcCategoryPO> poList) {
        try {
            checkState(CollectionUtils.isNotEmpty(poList));
            int ret = ugcCategoryMapper.insertBatch(poList);
            checkState(ret == poList.size());
        } catch (Exception e) {
            infraLog(LOGGER, InfraType.MYSQL, InfraCode.MYSQL_ERROR, e);
            throw AppSystemException.of(InfraCode.MYSQL_ERROR, e);
        }
    }

    public List<UgcCategoryPO> queryAll() {
        try {
            return ugcCategoryMapper.queryAll();
        } catch (Exception e) {
            infraLog(LOGGER, InfraType.MYSQL, InfraCode.MYSQL_ERROR, e);
            throw AppSystemException.of(InfraCode.MYSQL_ERROR, e);
        }
    }
}
