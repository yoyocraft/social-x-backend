package com.youyi.infra.http;

import com.youyi.common.util.GsonUtil;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.springframework.util.CollectionUtils;

import static com.google.common.net.HttpHeaders.ACCEPT;
import static com.google.common.net.HttpHeaders.CONTENT_TYPE;
import static com.google.common.net.MediaType.JSON_UTF_8;
import static com.youyi.common.constant.SymbolConstant.AND;
import static com.youyi.common.constant.SymbolConstant.EQUAL;
import static com.youyi.common.constant.SymbolConstant.QUESTION_MARK;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/03/09
 */
public class HttpUtil {

    private static final HttpClient httpClient = HttpClient.newHttpClient();

    public static CompletableFuture<String> sendPostRequest(
        String url,
        Object requestBody,
        Map<String, String> headers
    ) {
        String body = GsonUtil.toJson(requestBody);

        HttpRequest.Builder builder = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header(CONTENT_TYPE, JSON_UTF_8.toString())
            .header(ACCEPT, JSON_UTF_8.toString())
            .POST(HttpRequest.BodyPublishers.ofString(body));

        if (!CollectionUtils.isEmpty(headers)) {
            headers.forEach(builder::header);
        }
        HttpRequest request = builder.build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body);
    }

    public static CompletableFuture<String> sendGetRequest(
        String url,
        Map<String, String> params,
        Map<String, String> headers
    ) {
        String fullUrl = buildUrlWithParams(url, params);

        HttpRequest.Builder builder = HttpRequest.newBuilder()
            .uri(URI.create(fullUrl))
            .header(CONTENT_TYPE, JSON_UTF_8.toString())
            .header(ACCEPT, JSON_UTF_8.toString())
            .GET();

        // add custom headers
        if (!CollectionUtils.isEmpty(headers)) {
            headers.forEach(builder::header);
        }

        HttpRequest request = builder.build();
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body);
    }

    private static String buildUrlWithParams(String baseUrl, Map<String, String> params) {
        if (CollectionUtils.isEmpty(params)) {
            return baseUrl;
        }

        String query = params.entrySet().stream()
            .map(entry -> encodeParam(entry.getKey()) + EQUAL + encodeParam(entry.getValue()))
            .collect(Collectors.joining(AND));

        String separator = baseUrl.contains(QUESTION_MARK) ? AND : QUESTION_MARK;
        return baseUrl + separator + query;
    }

    private static String encodeParam(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
