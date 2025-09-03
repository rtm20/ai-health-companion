package com.habitcompanion.controller;

import com.habitcompanion.dto.AIMessageRequest;
import com.habitcompanion.dto.AIMessageResponse;
import com.habitcompanion.service.AIService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/ai")
public class AIController {

    @Autowired
    private AIService aiService;

    @PostMapping("/chat")
    public ResponseEntity<?> chatWithAI(@Valid @RequestBody AIMessageRequest request) {
        try {
            AIMessageResponse response = aiService.generateResponse(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/motivation/{habitId}")
    public ResponseEntity<?> getMotivation(@PathVariable UUID habitId) {
        try {
            AIMessageResponse response = aiService.generateMotivationalMessage(habitId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/suggestions")
    public ResponseEntity<?> getHabitSuggestions(@RequestParam String category) {
        try {
            List<String> suggestions = aiService.generateHabitSuggestions(category);
            return ResponseEntity.ok(suggestions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
