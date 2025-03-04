package com.youyi.domain.ugc.util;

import com.youyi.common.constant.SymbolConstant;
import org.apache.commons.lang3.StringUtils;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/03/04
 */
public class UgcContentUtil {

    public static String markdownToPlainText(String markdown) {
        // 1. 解析 Markdown 为 HTML
        Parser parser = Parser.builder().build();
        org.commonmark.node.Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        String html = renderer.render(document);

        // 2. 使用 Jsoup 解析 HTML 并保留换行
        Document doc = Jsoup.parse(html);
        for (Element br : doc.select("br")) {
            br.append(SymbolConstant.NEW_LINE);
        }
        for (Element p : doc.select("p")) {
            p.append(SymbolConstant.NEW_LINE);
        }

        // 3. 去除 HTML 标签并删除空行
        String text = doc.wholeText().replace("\\n", "\n");
        text = text.replaceAll("\n{2,}", "\n").trim();
        return text;
    }

    public static String polishGenUgcSummaryUserMessage(String title, String content) {
        if (StringUtils.isBlank(title)) {
            return content;
        }

        // 拼接 # title
        return SymbolConstant.HASH + SymbolConstant.SPACE + title +
            SymbolConstant.NEW_LINE +
            SymbolConstant.NEW_LINE +
            content;
    }

    public static String polishSummaryLine(StringBuilder lineBuilder) {
        String line = lineBuilder.toString().trim();
        // 检查数字列表的格式，确保数字后跟有空格
        if (line.matches("^\\d+\\.\\S.*")) {
            // 如果数字后没有空格，则加上空格
            line = line.replaceFirst("^([0-9]+\\.)(\\S)", "$1 $2");
        }

        // 对无序列表（ul）进行处理，确保符号后有空格
        if (line.startsWith("-") || line.startsWith("*")) {
            line = line.matches("^[*\\-](?!\\s).*") ? line.replaceFirst("^([*\\-])", "$1 ") : line;
        }
        return line;
    }
}
