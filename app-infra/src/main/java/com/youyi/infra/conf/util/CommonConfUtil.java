package com.youyi.infra.conf.util;

import com.youyi.common.base.BasePageRequest;
import com.youyi.infra.conf.core.ConfigKey;
import java.util.Collection;
import java.util.Objects;

import static com.youyi.infra.conf.core.Conf.getBooleanConfig;
import static com.youyi.infra.conf.core.Conf.getIntegerConfig;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/02/07
 */
public class CommonConfUtil {

    public static int calSize(BasePageRequest request) {
        if (Objects.isNull(request.getSize()) || request.getSize() <= 0) {
            return getIntegerConfig(ConfigKey.DEFAULT_PAGE_SIZE);
        }
        return Math.min(getIntegerConfig(ConfigKey.DEFAULT_PAGE_SIZE), request.getSize());
    }

    public static boolean checkHasMore(Collection<?> data) {
        return Objects.nonNull(data) && data.size() >= getIntegerConfig(ConfigKey.DEFAULT_PAGE_SIZE);
    }

    public static boolean hasScheduleOn() {
        return getBooleanConfig(ConfigKey.SCHEDULE_JOB_EXEC_AB_SWITCH, false);
    }
}
