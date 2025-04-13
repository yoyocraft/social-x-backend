package com.youyi;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/05
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestConfig.class)
public abstract class BaseIntegrationTest {
}
