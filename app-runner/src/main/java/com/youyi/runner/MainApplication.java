package com.youyi.runner;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2024/12/27
 */
@MapperScan(
    basePackages = {
        "com.youyi.infra.config.repository.mapper"
    },
    annotationClass = Mapper.class
)
@SpringBootApplication(
    scanBasePackages = {
        "com.youyi.runner",
        "com.youyi.core",
        "com.youyi.infra"
    }
)
public class MainApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
}
