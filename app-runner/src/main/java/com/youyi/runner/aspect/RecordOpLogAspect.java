package com.youyi.runner.aspect;

import com.google.common.collect.Lists;
import com.youyi.common.annotation.RecordOpLog;
import com.youyi.common.constant.SymbolConstant;
import com.youyi.common.type.aspect.AspectOrdered;
import com.youyi.common.util.GsonUtil;
import com.youyi.common.wrapper.ThreadPoolConfigWrapper;
import com.youyi.domain.audit.helper.OperationLogHelper;
import com.youyi.domain.audit.model.OperationLogDO;
import com.youyi.domain.audit.model.OperationLogExtraData;
import com.youyi.domain.user.helper.UserHelper;
import com.youyi.domain.user.model.UserDO;
import com.youyi.infra.conf.core.ConfigLoader;
import java.util.Arrays;
import java.util.List;
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

import static com.youyi.common.type.conf.ConfigKey.RECORD_OP_LOG_THREAD_POOL_CONFIG;
import static com.youyi.infra.conf.core.SystemConfigService.getCacheValue;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/05
 */
@Aspect
@Component
@RequiredArgsConstructor
public class RecordOpLogAspect implements ApplicationListener<ApplicationReadyEvent>, Ordered {

    // ========================== Constant ==========================
    public static final String SYSTEM_OPERATOR_ID = "-1";
    public static final String SYSTEM_OPERATOR_NAME = "SocialX System";

    private static final Logger LOGGER = LoggerFactory.getLogger(RecordOpLogAspect.class);

    private final OperationLogHelper operationLogHelper;
    private final UserHelper userHelper;
    private static ThreadPoolExecutor asyncRecordOpLogExecutor;

    /**
     * This method needs to be executed after initialization {@link ConfigLoader#afterSingletonsInstantiated()}
     * which init config cache
     */
    @Override
    public void onApplicationEvent(@Nonnull ApplicationReadyEvent event) {
        initAsyncExecutor();
    }

    @Pointcut("@annotation(com.youyi.common.annotation.RecordOpLog)")
    public void pointCut() {
    }

    @Before("pointCut()")
    public void asyncRecordOpLog(JoinPoint joinPoint) {
        if (asyncRecordOpLogExecutor == null) {
            LOGGER.error("Async executor for recording operation logs is not initialized.");
            return;
        }

        try {
            final OperationLogDO operationLogDO = preRecordOpLog(joinPoint);
            asyncRecordOpLogExecutor.execute(() -> {
                buildOperationLogDO(joinPoint, operationLogDO);
                doRecordOpLog(operationLogDO);
            });
        } catch (Exception e) {
            LOGGER.error("Failed to submit async task to record operation log: {}", e.getMessage(), e);
        }
    }

    private void doRecordOpLog(OperationLogDO operationLogDO) {
        try {
            operationLogHelper.recordOperationLog(operationLogDO);
        } catch (Exception e) {
            LOGGER.error("Error occurred while recording operation log: {}", e.getMessage(), e);
        }
    }

    private void initAsyncExecutor() {
        ThreadPoolConfigWrapper recordOpLogExecutorConfig = getCacheValue(RECORD_OP_LOG_THREAD_POOL_CONFIG, ThreadPoolConfigWrapper.class);
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

    private OperationLogDO preRecordOpLog(JoinPoint jp) {
        MethodSignature methodSignature = (MethodSignature) jp.getSignature();
        RecordOpLog recordOpLog = methodSignature.getMethod().getAnnotation(RecordOpLog.class);

        OperationLogDO operationLogDO = new OperationLogDO();

        operationLogDO.setOperationType(recordOpLog.opType().name());
        if (recordOpLog.system()) {
            operationLogDO.setOperatorId(SYSTEM_OPERATOR_ID);
            operationLogDO.setOperatorName(SYSTEM_OPERATOR_NAME);
        } else {
            // 以下操作必须要在异步线程之外做
            UserDO currentUser = userHelper.getCurrentUser();
            operationLogDO.setOperatorId(currentUser.getUserId());
            operationLogDO.setOperatorName(currentUser.getNickName());
        }
        return operationLogDO;
    }

    private void buildOperationLogDO(JoinPoint jp, OperationLogDO operationLogDO) {
        MethodSignature methodSignature = (MethodSignature) jp.getSignature();
        RecordOpLog recordOpLog = methodSignature.getMethod().getAnnotation(RecordOpLog.class);
        String methodName = methodSignature.getName();
        String className = jp.getTarget().getClass().getName();
        Class<?>[] types = methodSignature.getParameterTypes();
        String[] parameterNames = methodSignature.getParameterNames();
        Object[] parameterValues = jp.getArgs();

        List<String> paramValues = Lists.newArrayList();
        boolean desensitize = recordOpLog.desensitize();
        if (!desensitize) {
            paramValues = Arrays.stream(parameterValues).map(GsonUtil::toJson).toList();
        }
        OperationLogExtraData extraData = OperationLogExtraData.builder()
            .method(methodName)
            .className(className)
            .argType(StringUtils.join(types, SymbolConstant.COMMA))
            .argName(StringUtils.join(parameterNames, SymbolConstant.COMMA))
            .argValue(StringUtils.join(paramValues, SymbolConstant.COMMA))
            .build();
        operationLogDO.setExtraData(GsonUtil.toJson(extraData));
    }

    @Override
    public int getOrder() {
        return AspectOrdered.RECORD_OP_LOG.getOrder();
    }
}
