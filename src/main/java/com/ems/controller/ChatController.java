package com.ems.controller;

import com.ems.dto.gemini.ChatRequest;
import com.ems.dto.gemini.ChatResponse;
import com.ems.service.GeminiChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final GeminiChatService chatService;

    public ChatController(GeminiChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        String reply = chatService.chat(request.getMessage());

        ChatResponse response = new ChatResponse();
        response.setReply(reply);

        return ResponseEntity.ok(response);
    }
}
