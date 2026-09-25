package com.abhi.ai_chat.service;

import com.abhi.ai_chat.exception.SessionNotFoundException;
import com.abhi.ai_chat.model.ChatMessage;
import com.abhi.ai_chat.model.ChatSession;
import com.abhi.ai_chat.model.LlmProviderType;
import com.abhi.ai_chat.model.Role;
import com.abhi.ai_chat.provider.LlmProvider;
import com.abhi.ai_chat.provider.LlmProviderFactory;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final SessionStore sessionStore;
    private final LlmProviderFactory providerFactory;

    public ChatService(SessionStore sessionStore, LlmProviderFactory providerFactory) {
        this.sessionStore = sessionStore;
        this.providerFactory = providerFactory;
    }

    public ChatSession createSession(LlmProviderType providerType) {
        ChatSession session = new ChatSession(providerType);
        sessionStore.save(session);
        return session;
    }

    public ChatSession getSession(String sessionId) {
        return sessionStore.find(sessionId)
            .orElseThrow(() -> new SessionNotFoundException(sessionId));
    }

    public String sendMessage(String sessionId, String userMessage) {
        ChatSession session = getSession(sessionId);

        LlmProvider provider = providerFactory.getProvider(session.getProvider());
        String assistantReply = provider.sendMessage(session.getMessages(), userMessage);

        session.addMessage(ChatMessage.of(Role.USER, userMessage));
        session.addMessage(ChatMessage.of(Role.ASSISTANT, assistantReply));

        return assistantReply;
    }

    public void switchProvider(String sessionId, LlmProviderType newProvider) {
        ChatSession session = getSession(sessionId);
        session.setProvider(newProvider);
    }
}