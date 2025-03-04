package com.youyi.infra.ai;

import com.zhipu.oapi.ClientV4;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/03/04
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "social-x.ai")
public class AiConfiguration {

    private String apiKey;

    @Bean
    public ClientV4 glmClient() {
        return new ClientV4.Builder(apiKey).build();
    }
}
