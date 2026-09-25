package com.abhi.ai_chat.provider;

import com.abhi.ai_chat.model.ChatMessage;
import com.abhi.ai_chat.model.LlmProviderType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class OpenAiProvider implements LlmProvider {

    private final RestClient restClient;

    @Value("${openai.api-key}")
    private String apiKey;

    @Value("${openai.model:gpt-4o-mini}")
    private String model;

    public OpenAiProvider(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public LlmProviderType getType() {
        return LlmProviderType.OPENAI;
    }

    @Override
    public String sendMessage(List<ChatMessage> history, String newUserMessage) {
        List<Map<String, String>> messages = new ArrayList<>();
        for (ChatMessage m : history) {
            messages.add(Map.of(
                "role", m.getRole().name().toLowerCase(),
                "content", m.getContent()
            ));
        }
        messages.add(Map.of("role", "user", "content", newUserMessage));

        Map<String, Object> requestBody = Map.of(
            "model", model,
            "messages", messages
        );

        Map<String, Object> response = restClient.post()
            .uri("https://api.openai.com/v1/chat/completions")
            .header("Authorization", "Bearer " + apiKey)
            .header("Content-Type", "application/json")
            .body(requestBody)
            .retrieve()
            .body(Map.class);

        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
        Map<String, Object> firstChoice = choices.get(0);
        Map<String, String> message = (Map<String, String>) firstChoice.get("message");
        return message.get("content");
    }
}