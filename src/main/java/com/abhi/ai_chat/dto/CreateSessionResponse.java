package com.abhi.ai_chat.dto;

import com.abhi.ai_chat.model.LlmProviderType;

public class CreateSessionResponse {
    private String sessionId;
    private LlmProviderType provider;

    public CreateSessionResponse(String sessionId, LlmProviderType provider) {
        this.sessionId = sessionId;
        this.provider = provider;
    }

    public String getSessionId() {
        return sessionId;
    }

    public LlmProviderType getProvider() {
        return provider;
    }
}