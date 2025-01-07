package com.youyi.runner.aspect;

import com.google.common.collect.ImmutableMap;
import com.youyi.common.anno.RecordOpLog;
import com.youyi.common.constant.SymbolConstant;
import com.youyi.common.type.AspectOrdered;
import com.youyi.common.util.GsonUtil;
import com.youyi.common.wrapper.ThreadPoolConfigWrapper;
import com.youyi.domain.audit.model.OperationLogDO;
import com.youyi.domain.audit.helper.OperationLogHelper;
import com.youyi.infra.config.core.ConfigCacheService;
import com.youyi.infra.config.core.ConfigLoader;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import javax.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import static com.youyi.common.constant.SystemOperationConstant.LOG_ARG_TYPES_KEY;
import static com.youyi.common.constant.SystemOperationConstant.LOG_ARG_VALUES_KEY;
import static com.youyi.common.constant.SystemOperationConstant.LOG_CLASS_KEY;
import static com.youyi.common.constant.SystemOperationConstant.LOG_METHOD_KEY;
import static com.youyi.common.constant.SystemOperationConstant.SYSTEM_OPERATOR_ID;
import static com.youyi.common.constant.SystemOperationConstant.SYSTEM_OPERATOR_NAME;
import static com.youyi.common.type.ConfigKey.RECORD_OP_LOG_THREAD_POOL_CONFIG;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/05
 */
@Aspect
@Component
@RequiredArgsConstructor
public class RecordOpLogAspect implements ApplicationListener<ApplicationReadyEvent>, Ordered {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecordOpLogAspect.class);

    private final ConfigCacheService configCacheService;
    private final OperationLogHelper operationLogHelper;
    private static ThreadPoolExecutor asyncRecordOpLogExecutor;

    /**
     * This method needs to be executed after initialization {@link ConfigLoader#afterSingletonsInstantiated()}
     * which init config cache
     */
    @Override
    public void onApplicationEvent(@Nonnull ApplicationReadyEvent event) {
        initAsyncExecutor();
    }

    @Pointcut("@annotation(com.youyi.common.anno.RecordOpLog)")
    public void pointCut() {
    }

    @Before("pointCut()")
    public void asyncRecordOpLog(JoinPoint joinPoint) {
        if (asyncRecordOpLogExecutor == null) {
            LOGGER.error("Async executor for recording operation logs is not initialized.");
            return;
        }

        try {
            asyncRecordOpLogExecutor.execute(() -> doRecordOpLog(joinPoint));
        } catch (Exception e) {
            LOGGER.error("Failed to submit async task to record operation log: {}", e.getMessage(), e);
        }
    }

    private void doRecordOpLog(JoinPoint joinPoint) {
        try {
            OperationLogDO operationLogDO = buildOperationLogDO(joinPoint);
            LOGGER.info("Record operation log: {}", GsonUtil.toJson(operationLogDO));
            operationLogHelper.recordOperationLog(operationLogDO);
        } catch (Exception e) {
            LOGGER.error("Error occurred while recording operation log: {}", e.getMessage(), e);
        }
    }

    private void initAsyncExecutor() {
        ThreadPoolConfigWrapper recordOpLogExecutorConfig = configCacheService.getCacheValue(RECORD_OP_LOG_THREAD_POOL_CONFIG, ThreadPoolConfigWrapper.class);
        asyncRecordOpLogExecutor = new ThreadPoolExecutor(
            recordOpLogExecutorConfig.getCorePoolSize(),
            recordOpLogExecutorConfig.getMaximumPoolSize(),
            recordOpLogExecutorConfig.getKeepAliveTime(),
            recordOpLogExecutorConfig.getTimeUnit(),
            recordOpLogExecutorConfig.getQueue(),
            recordOpLogExecutorConfig.getThreadFactory(LOGGER),
            recordOpLogExecutorConfig.getRejectedHandler()
        );
    }

    private OperationLogDO buildOperationLogDO(JoinPoint jp) {
        MethodSignature methodSignature = (MethodSignature) jp.getSignature();
        String methodName = methodSignature.getName();
        String className = jp.getTarget().getClass().getSimpleName();
        Class<?>[] types = methodSignature.getParameterTypes();
        String[] parameterNames = methodSignature.getParameterNames();
        RecordOpLog recordOpLog = methodSignature.getMethod().getAnnotation(RecordOpLog.class);

        OperationLogDO operationLogDO = new OperationLogDO();

        operationLogDO.setOperationType(recordOpLog.opType().name());
        if (recordOpLog.system()) {
            operationLogDO.setOperatorId(SYSTEM_OPERATOR_ID);
            operationLogDO.setOperatorName(SYSTEM_OPERATOR_NAME);
        }
        // TODO youyi 2025/1/5 actual user info will be set in else block
        Map<String, Object> extraData = ImmutableMap.of(
            LOG_METHOD_KEY, methodName,
            LOG_CLASS_KEY, className,
            LOG_ARG_TYPES_KEY, StringUtils.join(types, SymbolConstant.COMMA),
            LOG_ARG_VALUES_KEY, StringUtils.join(parameterNames, SymbolConstant.COMMA)
        );
        operationLogDO.setExtraData(GsonUtil.toJson(extraData));

        return operationLogDO;
    }

    @Override
    public int getOrder() {
        return AspectOrdered.RECORD_OP_LOG.getOrder();
    }
}
