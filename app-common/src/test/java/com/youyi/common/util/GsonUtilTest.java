package com.youyi.common.util;

import com.google.common.collect.Lists;
import com.youyi.common.type.user.PermissionType;
import com.youyi.common.wrapper.ThreadPoolConfigWrapper;
import com.youyi.common.wrapper.UgcCategoryWrapper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
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

    @Test
    void testFromLocalDateTime() {
        LocalDateTime localDateTime = LocalDateTime.now();
        String dateJson = Assertions.assertDoesNotThrow(() -> GsonUtil.toJson(localDateTime));
        Assertions.assertTrue(StringUtils.isNotBlank(dateJson));
    }

    @Test
    void testCategories() {
        List<String> categories = Lists.newArrayList("求职", "后端", "前端", "客户端", "人工智能", "大数据", "云计算", "代码人生", "阅读", "开发工具");
        AtomicInteger priority = new AtomicInteger();
        List<UgcCategoryWrapper> categoryWrappers = categories.stream().map(category -> {
            String categoryId = RandomGenUtil.genUgcCategoryId();
            UgcCategoryWrapper wrapper = new UgcCategoryWrapper();
            wrapper.setCategoryId(categoryId);
            wrapper.setCategoryName(category);
            wrapper.setPriority(priority.getAndIncrement());
            return wrapper;
        }).toList();
        String json = Assertions.assertDoesNotThrow(() -> GsonUtil.toJson(categoryWrappers));
        Assertions.assertTrue(StringUtils.isNotBlank(json));
    }
}

// Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme