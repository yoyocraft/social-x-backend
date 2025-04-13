package com.youyi;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/04/13
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = {
    "com.youyi.runner",
    "com.youyi.domain",
    "com.youyi.infra"
})
public class TestConfig {
    // 测试配置
}
