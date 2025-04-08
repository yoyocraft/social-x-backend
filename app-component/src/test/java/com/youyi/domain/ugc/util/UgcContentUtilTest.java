package com.youyi.domain.ugc.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/03/04
 */
class UgcContentUtilTest {

    @Test
    void testMarkdownToPlainText() {
        String markdown = "# This is a Markdown Title.\nThis is a paragraph with **bold** text and *italic* text.\n![Image](image.jpg)\n";
        String plainText = UgcContentUtil.markdownToPlainText(markdown);
        Assertions.assertEquals(
            """
                This is a Markdown Title.
                This is a paragraph with bold text and italic text.""",
            plainText
        );
    }

    @Test
    void testPolishGenUgcSummaryUserMessage() {
        String title = "测试标题";
        String markdown = "# This is a Markdown Title.\nThis is a paragraph with **bold** text and *italic* text.\n![Image](image.jpg)\n";

        String userMessage = UgcContentUtil.polishGenUgcSummaryUserMessage(title, markdown);
        Assertions.assertEquals(
            """
                # 测试标题
                            
                # This is a Markdown Title.
                This is a paragraph with **bold** text and *italic* text.
                ![Image](image.jpg)
                """,
            userMessage
        );
    }
}

// Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme