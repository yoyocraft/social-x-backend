package com.youyi.common.util;

import com.google.common.collect.ImmutableSet;
import com.youyi.common.util.seq.IdSeqUtil;
import com.youyi.common.wrapper.ThreadPoolConfigWrapper;
import com.youyi.common.wrapper.UgcTagWrapper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
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
    void testFromLocalDateTime() {
        LocalDateTime localDateTime = LocalDateTime.now();
        String dateJson = Assertions.assertDoesNotThrow(() -> GsonUtil.toJson(localDateTime));
        Assertions.assertTrue(StringUtils.isNotBlank(dateJson));
    }

    @Test
    void testTags() {
        Set<String> articleTags = ImmutableSet.of(
            "面试技巧", "简历优化", "职场经验", "面试题", "求职心态", "Java", "Spring", "数据库",
            "微服务", "API设计", "Docker", "Redis", "JavaScript", "React", "Vue", "前端架构",
            "TypeScript", "CSS", "Web性能优化", "Android", "iOS", "Flutter", "React Native",
            "移动性能优化", "App架构", "机器学习", "深度学习", "自然语言处理", "计算机视觉", "AI算法",
            "TensorFlow", "PyTorch", "Hadoop", "Spark", "Kafka", "数据仓库", "数据挖掘", "数据分析",
            "AWS", "Azure", "GCP", "Kubernetes", "容器化", "云架构", "编程思维", "时间管理", "开发者心态",
            "职业规划", "学习方法", "编程书籍", "技术阅读", "经典书籍", "开发者必读", "Git", "VS Code", "IDE",
            "Postman", "DevOps"
        );
        AtomicInteger allTagPriority = new AtomicInteger();
        List<UgcTagWrapper> tagWrappers = articleTags.stream().map(tag -> {
            String tagId = IdSeqUtil.genUgcTagId();
            UgcTagWrapper wrapper = new UgcTagWrapper();
            wrapper.setTagId(tagId);
            wrapper.setTagName(tag);
            wrapper.setType(0);
            wrapper.setPriority(allTagPriority.getAndIncrement());
            return wrapper;
        }).toList();

        Set<String> interestTags = ImmutableSet.of(
            "面试技巧", "求职心态", "简历优化", "Java", "Spring", "微服务", "数据库", "JavaScript",
            "React", "前端架构", "Web性能优化", "Android", "iOS", "React Native", "App架构",
            "机器学习", "深度学习", "自然语言处理", "AI算法", "Hadoop", "Spark", "数据分析", "数据挖掘",
            "AWS", "Kubernetes", "Docker", "云架构", "编程思维", "时间管理", "开发者心态", "编程书籍",
            "技术阅读", "经典书籍", "Git", "VS Code", "DevOps"
        );
        AtomicInteger interestTagPriority = new AtomicInteger();
        List<UgcTagWrapper> interestTagWrappers = interestTags.stream().map(tag -> {
            String tagId = IdSeqUtil.genUgcTagId();
            UgcTagWrapper wrapper = new UgcTagWrapper();
            wrapper.setTagId(tagId);
            wrapper.setTagName(tag);
            wrapper.setType(1);
            wrapper.setPriority(interestTagPriority.getAndIncrement());
            return wrapper;
        }).toList();

        List<UgcTagWrapper> allTagWrappers = new ArrayList<>(tagWrappers);
        allTagWrappers.addAll(interestTagWrappers);

        String json = Assertions.assertDoesNotThrow(() -> GsonUtil.toJson(allTagWrappers));
        Assertions.assertTrue(StringUtils.isNotBlank(json));
    }
}

// Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme