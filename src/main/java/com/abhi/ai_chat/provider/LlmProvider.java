package com.abhi.ai_chat.provider;

import com.abhi.ai_chat.model.ChatMessage;
import com.abhi.ai_chat.model.LlmProviderType;

import java.util.List;

public interface LlmProvider {

    LlmProviderType getType();

    String sendMessage(List<ChatMessage> history, String newUserMessage);
}