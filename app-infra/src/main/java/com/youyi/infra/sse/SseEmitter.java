package com.youyi.infra.sse;

import javax.annotation.Nonnull;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpResponse;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/03/04
 */
public class SseEmitter extends org.springframework.web.servlet.mvc.method.annotation.SseEmitter {

    private static final MediaType TRANS_MEDIA_TYPE = MediaType.parseMediaType("text/event-stream;charset=UTF-8");

    public SseEmitter(Long timeout) {
        super(timeout);
    }

    @Override
    protected void extendResponse(@Nonnull ServerHttpResponse outputMessage) {
        super.extendResponse(outputMessage);

        // 设置响应头，支持中文
        outputMessage.getHeaders().setContentType(TRANS_MEDIA_TYPE);
    }
}
