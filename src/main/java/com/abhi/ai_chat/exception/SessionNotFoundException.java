package com.abhi.ai_chat.exception;

public class SessionNotFoundException extends RuntimeException {
    public SessionNotFoundException(String sessionId) {
        super("No chat session found with id: " + sessionId);
    }
}