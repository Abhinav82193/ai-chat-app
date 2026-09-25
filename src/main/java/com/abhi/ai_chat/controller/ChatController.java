package com.abhi.ai_chat.controller;

import com.abhi.ai_chat.dto.ChatRequest;
import com.abhi.ai_chat.dto.ChatResponse;
import com.abhi.ai_chat.dto.CreateSessionRequest;
import com.abhi.ai_chat.dto.CreateSessionResponse;
import com.abhi.ai_chat.dto.SwitchProviderRequest;
import com.abhi.ai_chat.model.ChatSession;
import com.abhi.ai_chat.service.ChatService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sessions")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public ResponseEntity<CreateSessionResponse> createSession(@Valid @RequestBody CreateSessionRequest request) {
        ChatSession session = chatService.createSession(request.getProvider());
        return ResponseEntity.ok(new CreateSessionResponse(session.getId(), session.getProvider()));
    }

    @PostMapping("/{sessionId}/messages")
    public ResponseEntity<ChatResponse> sendMessage(
            @PathVariable String sessionId,
            @Valid @RequestBody ChatRequest request) {
        String reply = chatService.sendMessage(sessionId, request.getMessage());
        return ResponseEntity.ok(new ChatResponse(reply));
    }

    @PutMapping("/{sessionId}/provider")
    public ResponseEntity<Void> switchProvider(
            @PathVariable String sessionId,
            @Valid @RequestBody SwitchProviderRequest request) {
        chatService.switchProvider(sessionId, request.getProvider());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<ChatSession> getSession(@PathVariable String sessionId) {
        return ResponseEntity.ok(chatService.getSession(sessionId));
    }
}