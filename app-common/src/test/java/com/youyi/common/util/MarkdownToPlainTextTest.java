package com.youyi.common.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/03/01
 */
class MarkdownToPlainTextTest {

    @Test
    void testMarkdownToPlainText() {
        String markdown = "# This is a Markdown Title.\nThis is a paragraph with **bold** text and *italic* text.\n![Image](image.jpg)\n";
        String plainText = CommonOperationUtil.markdownToPlainText(markdown);
        Assertions.assertEquals("""
            This is a Markdown Title.
            This is a paragraph with bold text and italic text.""", plainText);
    }

}
