package com.youyi.runner.aspect;

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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadPoolExecutor;
import javax.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import static com.youyi.common.constant.SystemConstant.SYSTEM_OPERATOR_ID;
import static com.youyi.common.constant.SystemConstant.SYSTEM_OPERATOR_NAME;
import static com.youyi.common.type.conf.ConfigKey.RECORD_OP_LOG_THREAD_POOL_CONFIG;
import static com.youyi.infra.conf.core.SystemConfigService.checkConfig;
import static com.youyi.infra.conf.core.SystemConfigService.getCacheValue;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/05
 */
@Aspect
@Component
@RequiredArgsConstructor
public class RecordOpLogAspect implements ApplicationListener<ApplicationReadyEvent>, Ordered {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecordOpLogAspect.class);

    private final OperationLogHelper operationLogHelper;
    private final UserHelper userHelper;
    private static ThreadPoolExecutor asyncRecordOpLogExecutor;
    private static final ExpressionParser expressionParser = new SpelExpressionParser();

    @Override
    public void onApplicationEvent(@Nonnull ApplicationReadyEvent event) {
        checkConfig(RECORD_OP_LOG_THREAD_POOL_CONFIG);
        initAsyncExecutor();
    }

    @Pointcut("@annotation(com.youyi.common.annotation.RecordOpLog)")
    public void pointCut() {
    }

    @AfterReturning(value = "pointCut()", returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result) {
        processOperationLog(joinPoint, result, null);
    }

    @AfterThrowing(value = "pointCut()", throwing = "exception")
    public void afterThrowing(JoinPoint joinPoint, Throwable exception) {
        processOperationLog(joinPoint, null, exception);
    }

    private void processOperationLog(JoinPoint joinPoint, Object result, Throwable exception) {
        if (asyncRecordOpLogExecutor == null) {
            LOGGER.error("Async executor for recording operation logs is not initialized.");
            return;
        }

        try {
            final OperationLogDO operationLogDO = preRecordOpLog(joinPoint);

            asyncRecordOpLogExecutor.execute(() -> {
                buildOperationLogDO(joinPoint, operationLogDO, result, exception);
                doRecordOpLog(operationLogDO);
            });
        } catch (Exception e) {
            LOGGER.error("Failed to submit async task to record operation log: {}", e.getMessage(), e);
        }
    }

    private void buildOperationLogDO(JoinPoint jp, OperationLogDO operationLogDO, Object result, Throwable exception) {
        MethodSignature methodSignature = (MethodSignature) jp.getSignature();
        RecordOpLog recordOpLog = methodSignature.getMethod().getAnnotation(RecordOpLog.class);
        String methodName = methodSignature.getName();
        String className = jp.getTarget().getClass().getName();
        String[] parameterNames = methodSignature.getParameterNames();
        // 记录参数值
        Object[] parameterValues = jp.getArgs();
        String[] fields = recordOpLog.fields();
        List<String> paramValues = filterFields(parameterNames, parameterValues, fields, recordOpLog.desensitize());

        // 构建额外数据
        OperationLogExtraData extraData = OperationLogExtraData.builder()
            .method(methodName)
            .className(className)
            .argValue(StringUtils.join(paramValues, SymbolConstant.COMMA))
            .build();

        // 记录返回值或异常
        if (exception != null) {
            extraData.setErrorMessage(exception.getMessage());
        } else if (result != null) {
            extraData.setReturnValue(GsonUtil.toJson(result));
        }

        operationLogDO.setExtraData(extraData);
    }

    private List<String> filterFields(String[] parameterNames, Object[] parameterValues, String[] fields, boolean desensitize) {
        if (fields.length > 0) {
            // 使用 SpEL 表达式解析字段值
            return Arrays.stream(fields)
                .map(field -> extractFieldValueWithSpEL(parameterNames, parameterValues, field))
                .filter(Objects::nonNull)
                .map(this::toStringValue)
                .toList();
        }

        if (desensitize) {
            return Collections.emptyList();
        }

        return Arrays.stream(parameterValues)
            .map(this::toStringValue)
            .toList();
    }

    private Object extractFieldValueWithSpEL(String[] parameterNames, Object[] parameterValues, String spelExpression) {
        try {
            StandardEvaluationContext context = new StandardEvaluationContext();
            for (int i = 0; i < parameterValues.length; i++) {
                context.setVariable(parameterNames[i], parameterValues[i]);
            }
            return expressionParser.parseExpression(spelExpression).getValue(context);
        } catch (Exception e) {
            LOGGER.warn("Failed to evaluate SpEL expression '{}': {}", spelExpression, e.getMessage());
            return null;
        }
    }

    private OperationLogDO preRecordOpLog(JoinPoint jp) {
        MethodSignature methodSignature = (MethodSignature) jp.getSignature();
        RecordOpLog recordOpLog = methodSignature.getMethod().getAnnotation(RecordOpLog.class);

        OperationLogDO operationLogDO = new OperationLogDO();

        operationLogDO.setOperationType(recordOpLog.opType());
        if (recordOpLog.system()) {
            operationLogDO.setOperatorId(SYSTEM_OPERATOR_ID);
            operationLogDO.setOperatorName(SYSTEM_OPERATOR_NAME);
        } else {
            UserDO currentUser = userHelper.getCurrentUser();
            operationLogDO.setOperatorId(currentUser.getUserId());
            operationLogDO.setOperatorName(currentUser.getNickName());
        }
        return operationLogDO;
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

    @Override
    public int getOrder() {
        return AspectOrdered.RECORD_OP_LOG.getOrder();
    }

    private String toStringValue(Object value) {
        // 如果是基本类型、包装类型或 String，直接返回 toString()
        if (value instanceof String || ClassUtils.isPrimitiveOrWrapper(value.getClass())) {
            return value.toString();
        }
        // 否则，将对象序列化为 JSON
        return GsonUtil.toJson(value);
    }
}
