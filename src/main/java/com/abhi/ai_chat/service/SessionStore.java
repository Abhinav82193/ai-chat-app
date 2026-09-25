package com.abhi.ai_chat.service;

import com.abhi.ai_chat.model.ChatSession;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionStore {

    private final Map<String, ChatSession> sessions = new ConcurrentHashMap<>();

    public void save(ChatSession session) {
        sessions.put(session.getId(), session);
    }

    public Optional<ChatSession> find(String sessionId) {
        return Optional.ofNullable(sessions.get(sessionId));
    }

    public boolean exists(String sessionId) {
        return sessions.containsKey(sessionId);
    }
}