package com.youyi.common.util;

import com.google.common.collect.Lists;
import com.youyi.common.type.user.PermissionType;
import com.youyi.common.wrapper.ThreadPoolConfigWrapper;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/05
 */
class GsonUtilTest {

    @Test
    void testToJson() {
        ThreadPoolConfigWrapper configWrapper = buildThreadPoolConfigWrapper();

        String json = Assertions.assertDoesNotThrow(() -> GsonUtil.toJson(configWrapper));
        Assertions.assertTrue(StringUtils.isNotBlank(json));
        String prettyJson = Assertions.assertDoesNotThrow(() -> GsonUtil.toPrettyJson(configWrapper));
        Assertions.assertTrue(StringUtils.isNotBlank(prettyJson));
        ThreadPoolConfigWrapper wrapper = Assertions.assertDoesNotThrow(() -> GsonUtil.fromJson(json, ThreadPoolConfigWrapper.class));
        Assertions.assertAll(
            () -> Assertions.assertEquals(configWrapper.getCorePoolSize(), wrapper.getCorePoolSize()),
            () -> Assertions.assertEquals(configWrapper.getMaximumPoolSize(), wrapper.getMaximumPoolSize()),
            () -> Assertions.assertEquals(configWrapper.getKeepAliveTime(), wrapper.getKeepAliveTime()),
            () -> Assertions.assertEquals(configWrapper.getQueueCapacity(), wrapper.getQueueCapacity()),
            () -> Assertions.assertEquals(configWrapper.getThreadNameFormat(), wrapper.getThreadNameFormat()),
            () -> Assertions.assertEquals(configWrapper.getQueueType(), wrapper.getQueueType()),
            () -> Assertions.assertEquals(configWrapper.getRejectedHandlerType(), wrapper.getRejectedHandlerType())
        );
    }

    private ThreadPoolConfigWrapper buildThreadPoolConfigWrapper() {
        ThreadPoolConfigWrapper configWrapper = new ThreadPoolConfigWrapper();
        configWrapper.setCorePoolSize(1);
        configWrapper.setMaximumPoolSize(2);
        configWrapper.setKeepAliveTime(100_000L);
        configWrapper.setQueueCapacity(1000);
        configWrapper.setThreadNameFormat("async-record-log-%s");
        configWrapper.setQueueType(ThreadPoolConfigWrapper.QueueType.LINKED_BLOCKING_QUEUE.name());
        configWrapper.setRejectedHandlerType(ThreadPoolConfigWrapper.RejectedExecutionHandlerType.CALLER_RUNS_POLICY.name());
        return configWrapper;
    }

    @Test
    void testFromJson() {
        List<PermissionType> permissions = Lists.newArrayList(
            PermissionType.CREATE_CONFIG,
            PermissionType.READ_CONFIG,
            PermissionType.UPDATE_CONFIG,
            PermissionType.DELETE_CONFIG
        );

        String json = GsonUtil.toJson(permissions);
        List<PermissionType> permissionTypes = Assertions.assertDoesNotThrow(() -> GsonUtil.fromJson(json, List.class, PermissionType.class));
        Assertions.assertTrue(CollectionUtils.isNotEmpty(permissionTypes));
    }
}

// Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme