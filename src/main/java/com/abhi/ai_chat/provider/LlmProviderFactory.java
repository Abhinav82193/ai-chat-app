package com.abhi.ai_chat.provider;

import com.abhi.ai_chat.model.LlmProviderType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class LlmProviderFactory {

    private final Map<LlmProviderType, LlmProvider> providers;

    public LlmProviderFactory(List<LlmProvider> providerList) {
        this.providers = providerList.stream()
            .collect(Collectors.toMap(LlmProvider::getType, Function.identity()));
    }

    public LlmProvider getProvider(LlmProviderType type) {
        LlmProvider provider = providers.get(type);
        if (provider == null) {
            throw new IllegalArgumentException("No provider registered for type: " + type);
        }
        return provider;
    }
}