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
public class GeminiProvider implements LlmProvider {

    private final RestClient restClient;

    @Value("${gemini.api-key}")
    private String apiKey;

    @Value("${gemini.model:gemini-1.5-flash}")
    private String model;

    public GeminiProvider(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public LlmProviderType getType() {
        return LlmProviderType.GEMINI;
    }

    @Override
    public String sendMessage(List<ChatMessage> history, String newUserMessage) {
        List<Map<String, Object>> contents = new ArrayList<>();
        for (ChatMessage m : history) {
            String role = m.getRole().name().equalsIgnoreCase("ASSISTANT") ? "model" : "user";
            contents.add(Map.of(
                "role", role,
                "parts", List.of(Map.of("text", m.getContent()))
            ));
        }
        contents.add(Map.of(
            "role", "user",
            "parts", List.of(Map.of("text", newUserMessage))
        ));

        Map<String, Object> requestBody = Map.of("contents", contents);

        String uri = "https://generativelanguage.googleapis.com/v1beta/models/"
            + model + ":generateContent?key=" + apiKey;

        Map<String, Object> response = restClient.post()
            .uri(uri)
            .header("Content-Type", "application/json")
            .body(requestBody)
            .retrieve()
            .body(Map.class);

        List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
        Map<String, Object> firstCandidate = candidates.get(0);
        Map<String, Object> content = (Map<String, Object>) firstCandidate.get("content");
        List<Map<String, String>> parts = (List<Map<String, String>>) content.get("parts");
        return parts.get(0).get("text");
    }
}