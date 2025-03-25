package com.youyi.domain.ugc.repository;

import com.youyi.common.base.BaseRepository;
import com.youyi.common.type.InfraCode;
import com.youyi.common.type.InfraType;
import com.youyi.domain.ugc.repository.mapper.UgcCategoryMapper;
import com.youyi.domain.ugc.repository.po.UgcCategoryPO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/25
 */
@Repository
@RequiredArgsConstructor
public class UgcCategoryRepository extends BaseRepository {

    private static final Logger logger = LoggerFactory.getLogger(UgcCategoryRepository.class);

    private final UgcCategoryMapper ugcCategoryMapper;

    @Override
    protected Logger getLogger() {
        return logger;
    }

    @Override
    protected InfraType getInfraType() {
        return InfraType.MYSQL;
    }

    @Override
    protected InfraCode getInfraCode() {
        return InfraCode.MYSQL_ERROR;
    }

    public void insertBatchCategory(List<UgcCategoryPO> poList) {
        checkState(CollectionUtils.isNotEmpty(poList));
        int ret = executeWithExceptionHandling(() -> ugcCategoryMapper.insertBatch(poList));
        checkState(ret == poList.size());
    }

    public List<UgcCategoryPO> queryAll() {
        return executeWithExceptionHandling(ugcCategoryMapper::queryAll);
    }

    public long count() {
        return executeWithExceptionHandling(ugcCategoryMapper::count);
    }

    public List<UgcCategoryPO> queryByType(Integer type) {
        checkNotNull(type);
        return executeWithExceptionHandling(() -> ugcCategoryMapper.queryByType(type));
    }

    public UgcCategoryPO queryByCategoryId(String categoryId) {
        checkState(StringUtils.isNotBlank(categoryId));
        return executeWithExceptionHandling(() -> ugcCategoryMapper.queryByCategoryId(categoryId));
    }
}
