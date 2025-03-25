package com.youyi.domain.ugc.repository;

import com.youyi.common.base.BaseRepository;
import com.youyi.common.type.InfraCode;
import com.youyi.common.type.InfraType;
import com.youyi.domain.ugc.repository.mapper.UgcTagMapper;
import com.youyi.domain.ugc.repository.po.UgcTagPO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
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
public class UgcTagRepository extends BaseRepository {

    private static final Logger logger = LoggerFactory.getLogger(UgcTagRepository.class);

    private final UgcTagMapper ugcTagMapper;

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

    public void insertBatchTag(List<UgcTagPO> poList) {
        checkState(CollectionUtils.isNotEmpty(poList));
        int ret = executeWithExceptionHandling(() -> ugcTagMapper.insertBatch(poList));
        checkState(ret == poList.size());
    }

    public List<UgcTagPO> queryByType(Integer type) {
        checkNotNull(type);
        return executeWithExceptionHandling(() -> ugcTagMapper.queryByType(type));
    }

    public List<UgcTagPO> queryAll() {
        return executeWithExceptionHandling(ugcTagMapper::queryAll);
    }

    public long count() {
        return executeWithExceptionHandling(ugcTagMapper::count);
    }
}
