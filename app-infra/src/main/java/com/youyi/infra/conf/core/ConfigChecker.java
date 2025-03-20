package com.youyi.infra.conf.core;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/03/19
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ConfigChecker implements ApplicationListener<ApplicationReadyEvent> {
    @Override
    public void onApplicationEvent(@Nonnull ApplicationReadyEvent event) {
        checkConfigs();
    }

    private void checkConfigs() {
        ConfigKey[] configs = ConfigKey.values();
        List<String> errorMessages = new ArrayList<>();

        for (ConfigKey config : configs) {
            try {
                Conf.checkConfig(config);
            } catch (IllegalStateException e) {
                errorMessages.add(e.getMessage());
            }
        }

        if (!errorMessages.isEmpty()) {
            throw new IllegalStateException(
                "Configuration errors found:\n" + String.join("\n", errorMessages)
            );
        }
    }
}
