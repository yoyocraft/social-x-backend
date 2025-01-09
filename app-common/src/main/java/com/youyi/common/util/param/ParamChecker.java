package com.youyi.common.util.param;

import com.youyi.common.util.GsonUtil;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.youyi.common.constant.ErrorCodeConstant.INVALID_PARAM;
import static com.youyi.common.constant.ValidateRegex.VALIDATE_EMAIL_PATTERN;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2024/12/28
 */
@FunctionalInterface
public interface ParamChecker<T> {

    Logger LOGGER = LoggerFactory.getLogger(ParamChecker.class);

    @Nonnull
    CheckResult validate(T data);

    default CheckResult validateWithLog(T data) {
        CheckResult ret = this.validate(data);
        if (CheckResult.isSuccess(ret)) {
            return CheckResult.success();
        }

        LOGGER.warn("invalid param:{}, result:{}", GsonUtil.toJson(data), GsonUtil.toJson(ret));
        return ret;
    }

    static ParamChecker<String> notBlankChecker(String errorMsg) {
        return param -> StringUtils.isNotBlank(param)
            ? CheckResult.success()
            : CheckResult.of(INVALID_PARAM, errorMsg);
    }

    static ParamChecker<String> notBlankAndLengthChecker(int maxLength, String errorMsg) {
        return param -> StringUtils.isBlank(param) || param.length() > maxLength
            ? CheckResult.of(INVALID_PARAM, errorMsg + "，长度不能超过" + maxLength)
            : CheckResult.success();
    }

    static ParamChecker<String> notBlankAndLengthChecker(int maxLength) {
        return notBlankAndLengthChecker(maxLength, INVALID_PARAM);
    }

    static ParamChecker<Collection<?>> collectionNotEmptyChecker(String errorMsg) {
        return collection -> CollectionUtils.isNotEmpty(collection)
            ? CheckResult.success()
            : CheckResult.of(INVALID_PARAM, errorMsg);
    }

    static ParamChecker<Long> notNullAndPositiveChecker(String errorMsg) {
        return param -> param != null && param > 0
            ? CheckResult.success()
            : CheckResult.of(INVALID_PARAM, errorMsg);
    }

    static ParamChecker<Long> rangeCloseChecker(long min, long max, String errorMsg) {
        return param -> param != null && param >= min && param <= max
            ? CheckResult.success()
            : CheckResult.of(INVALID_PARAM, errorMsg);
    }

    static ParamChecker<Long> greaterThanChecker(long min, String errorMsg) {
        return param -> param != null && param > min
            ? CheckResult.success()
            : CheckResult.of(INVALID_PARAM, errorMsg);
    }

    static ParamChecker<Integer> greaterThanOrEqualChecker(int min, String errorMsg) {
        return param -> param != null && param >= min
            ? CheckResult.success()
            : CheckResult.of(INVALID_PARAM, errorMsg);
    }

    static ParamChecker<Long> lessThanChecker(long max, String errorMsg) {
        return param -> param != null && param < max
            ? CheckResult.success()
            : CheckResult.of(INVALID_PARAM, errorMsg);
    }

    static ParamChecker<Long> lessThanOrEqualChecker(long max, String errorMsg) {
        return param -> param != null && param <= max
            ? CheckResult.success()
            : CheckResult.of(INVALID_PARAM, errorMsg);
    }

    static <T extends Enum<T>> ParamChecker<String> enumExistChecker(Class<T> enumClass, String errorMsg) {
        return param -> {
            boolean exist = Stream.of(enumClass.getEnumConstants())
                .anyMatch(e -> StringUtils.equals(e.name(), param));
            return exist
                ? CheckResult.success()
                : CheckResult.of(INVALID_PARAM, errorMsg);
        };
    }

    static <T extends Enum<T>> ParamChecker<String> enumExistChecker(Class<T> enumClass, Predicate<String> predicate, String errorMsg) {
        return param -> {
            boolean validate = predicate.test(param) && Stream.of(enumClass.getEnumConstants())
                .anyMatch(e -> StringUtils.equals(e.name(), param));
            return validate
                ? CheckResult.success()
                : CheckResult.of(INVALID_PARAM, errorMsg);
        };
    }

    static ParamChecker<String> emailChecker(String errorMsg) {
        return param -> {
            boolean validate = StringUtils.isNotBlank(param) && VALIDATE_EMAIL_PATTERN.matcher(param).matches();
            return validate
                ? CheckResult.success()
                : CheckResult.of(INVALID_PARAM, errorMsg);
        };
    }
}
