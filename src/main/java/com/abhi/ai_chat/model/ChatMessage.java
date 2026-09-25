package com.abhi.ai_chat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    private Role role;
    private String content;
    private Instant timestamp;

    public static ChatMessage of(Role role, String content) {
        return new ChatMessage(role, content, Instant.now());
    }
}