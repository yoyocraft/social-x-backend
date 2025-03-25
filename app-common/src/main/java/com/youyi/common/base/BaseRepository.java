package com.youyi.common.base;

import com.youyi.common.exception.AppSystemException;
import com.youyi.common.type.InfraCode;
import com.youyi.common.type.InfraType;
import org.slf4j.Logger;

import static com.youyi.common.util.LogUtil.infraLog;

/**
 * 基础仓储类，提供通用的异常处理和日志记录
 *
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/03/25
 */
public abstract class BaseRepository {

    protected abstract Logger getLogger();
    
    protected abstract InfraType getInfraType();
    
    protected abstract InfraCode getInfraCode();

    /**
     * 执行数据库操作并处理异常
     *
     * @param operation 数据库操作
     * @param <T> 返回类型
     * @return 操作结果
     */
    protected <T> T executeWithExceptionHandling(RepositoryOperation<T> operation) {
        try {
            return operation.execute();
        } catch (Exception e) {
            infraLog(getLogger(), getInfraType(), getInfraCode(), e);
            throw AppSystemException.of(getInfraCode(), e);
        }
    }

    /**
     * 执行无返回值的数据库操作并处理异常
     *
     * @param operation 数据库操作
     */
    protected void executeWithExceptionHandling(VoidRepositoryOperation operation) {
        try {
            operation.execute();
        } catch (Exception e) {
            infraLog(getLogger(), getInfraType(), getInfraCode(), e);
            throw AppSystemException.of(getInfraCode(), e);
        }
    }

    /**
     * 数据库操作接口
     */
    @FunctionalInterface
    public interface RepositoryOperation<T> {
        T execute() throws Exception;
    }

    /**
     * 无返回值的数据库操作接口
     */
    @FunctionalInterface
    public interface VoidRepositoryOperation {
        void execute() throws Exception;
    }
}