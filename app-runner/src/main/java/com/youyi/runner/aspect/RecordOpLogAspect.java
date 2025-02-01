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
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import javax.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
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
    private static final ExpressionParser expressionParser = new SpelExpressionParser();
    private static ThreadPoolExecutor asyncRecordOpLogExecutor;

    private final OperationLogHelper operationLogHelper;
    private final UserHelper userHelper;

    @Override
    public void onApplicationEvent(@Nonnull ApplicationReadyEvent event) {
        checkConfig(RECORD_OP_LOG_THREAD_POOL_CONFIG);
        initAsyncExecutor();
    }

    @Override
    public int getOrder() {
        return AspectOrdered.RECORD_OP_LOG.getOrder();
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
        Method method = methodSignature.getMethod();
        RecordOpLog recordOpLog = method.getAnnotation(RecordOpLog.class);
        String className = jp.getTarget().getClass().getName();
        String[] parameterNames = methodSignature.getParameterNames();
        Class<?>[] parameterTypes = methodSignature.getParameterTypes();
        // 记录参数值
        Object[] parameterValues = jp.getArgs();
        // 过滤出需要记录的字段
        List<String> paramValues = filterFields(parameterNames, parameterValues, recordOpLog.fields(), recordOpLog.desensitize());

        List<String> paramTypes = Arrays.stream(parameterTypes)
            .map(Class::getName)
            .toList();
        // 构建 Extra Info
        OperationLogExtraData extraData = OperationLogExtraData.builder()
            .method(method.getName())
            .className(className)
            .argType(StringUtils.join(paramTypes, SymbolConstant.COMMA))
            .argName(StringUtils.join(parameterNames, SymbolConstant.COMMA))
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

    /**
     * 1. 非脱敏 && fields 为空，记录所有参数值
     * 2. 脱敏 && fields 为空，不记录参数值
     * 3. 非脱敏 && fields 不为空，记录 fields 中配置的字段值
     * 4. 脱敏 && fields 不为空，记录 fields 中配置的字段值
     */
    private List<String> filterFields(String[] parameterNames, Object[] parameterValues, String[] fields, boolean desensitize) {
        // 配置了 fields，优先解析
        if (fields.length > 0) {
            Map<String, Object> fieldMap = new HashMap<>(fields.length);
            for (String field : fields) {
                // 使用 SpEL 表达式解析字段值
                Object fieldValue = extractFieldValueWithSpEL(parameterNames, parameterValues, field);
                if (fieldValue != null) {
                    fieldMap.put(field, fieldValue);
                }
            }
            return Collections.singletonList(GsonUtil.toJson(fieldMap));
        }

        // 脱敏，不记录参数值
        if (desensitize) {
            return Collections.emptyList();
        }

        // 非脱敏，记录所有参数值
        return Arrays.stream(parameterValues)
            .map(GsonUtil::toJson)
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
}
