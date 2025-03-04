package com.youyi.infra.ai;

import com.youyi.common.exception.AppBizException;
import com.youyi.common.type.InfraCode;
import com.youyi.common.type.InfraType;
import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.ChatCompletionRequest;
import com.zhipu.oapi.service.v4.model.ChatMessage;
import com.zhipu.oapi.service.v4.model.ChatMessageRole;
import com.zhipu.oapi.service.v4.model.ModelApiResponse;
import com.zhipu.oapi.service.v4.model.ModelData;
import io.reactivex.Flowable;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static com.youyi.common.constant.SymbolConstant.EMPTY;
import static com.youyi.common.type.ReturnCode.SYSTEM_ERROR;
import static com.youyi.common.util.LogUtil.infraLog;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/03/04
 */
@Component
@RequiredArgsConstructor
public class AiClient {
    private static final Logger logger = LoggerFactory.getLogger(AiClient.class);

    private final ClientV4 glmClient;

    private static final float STABLE_TEMPERATURE = 0.05f;

    private static final float UNSTABLE_TEMPERATURE = 0.99f;

    public Flowable<ModelData> doStreamStableRequest(String systemMessage, String userMessage) {
        return doStreamRequest(systemMessage, userMessage, STABLE_TEMPERATURE);
    }

    public Flowable<ModelData> doStreamUnstableRequest(String systemMessage, String userMessage) {
        return doStreamRequest(systemMessage, userMessage, UNSTABLE_TEMPERATURE);
    }

    public Flowable<ModelData> doStreamRequest(String systemMessage, String userMessage, Float temperature) {
        ChatMessage systemChatMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), systemMessage);
        ChatMessage userChatMessage = new ChatMessage(ChatMessageRole.USER.value(), userMessage);
        List<ChatMessage> messages = List.of(systemChatMessage, userChatMessage);
        String reqId = String.valueOf(System.currentTimeMillis());
        return doStreamRequest(messages, temperature, reqId);
    }

    public Flowable<ModelData> doStreamRequest(List<ChatMessage> messages, Float temperature, String reqId) {
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
            .model(Constants.ModelChatGLM4Flash)
            .stream(Boolean.TRUE)
            .invokeMethod(Constants.invokeMethod)
            .temperature(temperature)
            .messages(messages)
            .requestId(reqId)
            .build();
        try {
            ModelApiResponse invokeModelApiResp = glmClient.invokeModelApi(chatCompletionRequest);
            return invokeModelApiResp.getFlowable();
        } catch (Exception e) {
            infraLog(logger, InfraType.AI, InfraCode.AI_ERROR, e);
            return Flowable.error(AppBizException.of(SYSTEM_ERROR, e.getMessage()));
        }
    }

    public String doSyncStableRequest(String systemMessage, String userMessage) {
        return doSyncRequest(systemMessage, userMessage, STABLE_TEMPERATURE);
    }

    public String doSyncUnstableRequest(String systemMessage, String userMessage) {
        return doSyncRequest(systemMessage, userMessage, UNSTABLE_TEMPERATURE);
    }

    public String doSyncRequest(String systemMessage, String userMessage, Float temperature) {
        return doRequest(systemMessage, userMessage, Boolean.FALSE, temperature);
    }

    public String doRequest(String systemMessage, String userMessage, Boolean stream, Float temperature) {
        ChatMessage systemChatMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), systemMessage);
        ChatMessage userChatMessage = new ChatMessage(ChatMessageRole.USER.value(), userMessage);
        List<ChatMessage> messages = List.of(systemChatMessage, userChatMessage);
        String reqId = String.valueOf(System.currentTimeMillis());
        return doRequest(messages, stream, temperature, reqId);
    }

    public String doRequest(List<ChatMessage> messages, Boolean stream, Float temperature, String reqId) {
        ChatCompletionRequest request = ChatCompletionRequest.builder()
            .model(Constants.ModelChatGLM4Flash)
            .stream(stream)
            .invokeMethod(Constants.invokeMethod)
            .temperature(temperature)
            .messages(messages)
            .requestId(reqId)
            .build();
        try {
            ModelApiResponse invokeModelApiResp = glmClient.invokeModelApi(request);
            ChatMessage result = invokeModelApiResp.getData().getChoices().get(0).getMessage();
            return result.getContent().toString();
        } catch (Exception e) {
            infraLog(logger, InfraType.AI, InfraCode.AI_ERROR, e);
            return EMPTY;
        }
    }

}
