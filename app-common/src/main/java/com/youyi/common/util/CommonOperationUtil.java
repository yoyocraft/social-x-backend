package com.youyi.common.util;

import com.youyi.common.constant.SymbolConstant;
import com.youyi.common.exception.AppBizException;
import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.apache.commons.lang3.StringUtils;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import static com.youyi.common.type.ReturnCode.TOO_MANY_REQUEST;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/17
 */
public class CommonOperationUtil {

    public static void tooManyRequestError() {
        throw AppBizException.of(TOO_MANY_REQUEST);
    }

    public static String buildFullPath(String... paths) {
        return StringUtils.join(paths, File.separator);
    }

    public static long date2Timestamp(LocalDateTime date) {
        return date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

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
}
