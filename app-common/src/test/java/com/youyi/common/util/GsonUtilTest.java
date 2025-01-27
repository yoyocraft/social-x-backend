package com.youyi.common.util;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.youyi.common.type.ugc.UgcTagType;
import com.youyi.common.type.user.PermissionType;
import com.youyi.common.wrapper.ThreadPoolConfigWrapper;
import com.youyi.common.wrapper.UgcCategoryWrapper;
import com.youyi.common.wrapper.UgcTagWrapper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
            String categoryId = IdSeqUtil.genUgcCategoryId();
            UgcCategoryWrapper wrapper = new UgcCategoryWrapper();
            wrapper.setCategoryId(categoryId);
            wrapper.setCategoryName(category);
            wrapper.setPriority(priority.getAndIncrement());
            return wrapper;
        }).toList();
        String json = Assertions.assertDoesNotThrow(() -> GsonUtil.toJson(categoryWrappers));
        Assertions.assertTrue(StringUtils.isNotBlank(json));
    }

    @Test
    void testTags() {
        Set<String> allTags = ImmutableSet.of(
            "编程挑战", "学习新技术", "技术笔记", "职业发展", "数据科学", "前端框架", "后端开发", "开源项目贡献", "技术交流", "AI与大数据", "全栈开发", "软件架构", "代码复审", "算法设计", "面试准备", "技术博客写作", "技术工具推荐", "团队协作", "技术大会", "程序设计", "云服务", "自动化测试", "产品管理", "敏捷开发", "跨平台开发", "UI/UX设计", "技术栈更新", "Python编程", "数据可视化", "机器学习入门", "编程语言比较", "数据结构与算法", "个人成长", "职场社交", "技术讲座", "代码挑战", "技术管理", "后端", "前端", "客户端", "大数据", "人工智能", "机器学习", "深度学习", "区块链", "云计算", "微服务", "DevOps", "测试开发", "代码人生", "技术博客", "阅读", "开发工具", "求职", "职业规划", "开源项目", "技术分享", "算法", "架构设计", "数据库", "分布式系统", "网络安全", "容器化", "Kubernetes", "高并发", "系统设计", "产品设计", "用户体验", "技术面试", "职业成长", "职场经验", "代码优化", "性能调优", "AI生成内容", "低代码开发"
        );
        AtomicInteger allTagPriority = new AtomicInteger();
        List<UgcTagWrapper> tagWrappers = allTags.stream().map(tag -> {
            String tagId = IdSeqUtil.genUgcTagId();
            UgcTagWrapper wrapper = new UgcTagWrapper();
            wrapper.setTagId(tagId);
            wrapper.setTagName(tag);
            wrapper.setType(UgcTagType.FOR_ARTICLE.getType());
            wrapper.setPriority(allTagPriority.getAndIncrement());
            return wrapper;
        }).toList();

        Set<String> interestTags = ImmutableSet.of(
            "编程挑战", "学习新技术", "技术笔记", "职业发展", "数据科学", "前端框架", "后端开发", "开源项目贡献", "技术交流", "AI与大数据", "全栈开发", "软件架构", "代码复审", "算法设计", "面试准备", "技术博客写作", "技术工具推荐", "团队协作", "技术大会", "程序设计", "云服务", "自动化测试", "产品管理", "敏捷开发", "跨平台开发", "UI/UX设计", "技术栈更新", "Python编程", "数据可视化", "机器学习入门", "编程语言比较", "数据结构与算法", "个人成长", "职场社交", "技术讲座", "代码挑战", "技术管理"
        );
        AtomicInteger interestTagPriority = new AtomicInteger();
        List<UgcTagWrapper> interestTagWrappers = interestTags.stream().map(tag -> {
            String tagId = IdSeqUtil.genUgcTagId();
            UgcTagWrapper wrapper = new UgcTagWrapper();
            wrapper.setTagId(tagId);
            wrapper.setTagName(tag);
            wrapper.setType(UgcTagType.FOR_USER_INTEREST.getType());
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