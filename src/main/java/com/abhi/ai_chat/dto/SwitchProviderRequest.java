package com.abhi.ai_chat.dto;

import com.abhi.ai_chat.model.LlmProviderType;
import jakarta.validation.constraints.NotNull;

public class SwitchProviderRequest {

    @NotNull(message = "provider is required")
    private LlmProviderType provider;

    public LlmProviderType getProvider() {
        return provider;
    }

    public void setProvider(LlmProviderType provider) {
        this.provider = provider;
    }
}