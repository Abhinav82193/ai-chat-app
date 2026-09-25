package com.abhi.ai_chat.model;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class ChatSession {
    private final String id;
    private final Instant createdAt;
    @Setter
    private LlmProviderType provider;
    private final List<ChatMessage> messages = new ArrayList<>();

    public ChatSession(LlmProviderType provider) {
        this.id = UUID.randomUUID().toString();
        this.createdAt = Instant.now();
        this.provider = provider;
    }

    public void addMessage(ChatMessage message) {
        messages.add(message);
    }
}