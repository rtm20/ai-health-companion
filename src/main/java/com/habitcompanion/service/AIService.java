package com.habitcompanion.service;

import com.habitcompanion.dto.AIMessageRequest;
import com.habitcompanion.dto.AIMessageResponse;
import com.habitcompanion.model.AIConversation;
import com.habitcompanion.model.Habit;
import com.habitcompanion.model.User;
import com.habitcompanion.repository.AIConversationRepository;
import com.habitcompanion.repository.HabitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class AIService {

    @Autowired
    private AIConversationRepository conversationRepository;

    @Autowired
    private HabitRepository habitRepository;

    @Autowired
    private AuthService authService;

    @Value("${openai.api.key}")
    private String openAiApiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    public AIMessageResponse generateResponse(AIMessageRequest request) {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("User not authenticated");
        }

        String prompt = buildPrompt(request, currentUser);
        String aiResponse = callOpenAI(prompt);

        // Save conversation
        AIConversation conversation = new AIConversation(
                currentUser, request.getMessage(), aiResponse, request.getContextType());
        conversationRepository.save(conversation);

        return new AIMessageResponse(aiResponse, request.getContextType());
    }

    public AIMessageResponse generateMotivationalMessage(UUID habitId) {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("User not authenticated");
        }

        Habit habit = habitRepository.findById(habitId)
                .orElseThrow(() -> new RuntimeException("Habit not found"));

        if (!habit.getUser().equals(currentUser)) {
            throw new RuntimeException("Unauthorized access to habit");
        }

        String prompt = buildMotivationalPrompt(habit);
        String aiResponse = callOpenAI(prompt);

        // Save conversation
        AIConversation conversation = new AIConversation(
                currentUser, "Generate motivation for " + habit.getTitle(), 
                aiResponse, "motivation");
        conversationRepository.save(conversation);

        return new AIMessageResponse(aiResponse, "motivation");
    }

    public List<String> generateHabitSuggestions(String category) {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("User not authenticated");
        }

        List<Habit> existingHabits = habitRepository.findByUserAndIsActiveTrue(currentUser);
        String existingHabitsText = existingHabits.stream()
                .map(Habit::getTitle)
                .reduce("", (a, b) -> a + ", " + b);

        String prompt = String.format(
                "Suggest 3 simple micro-habits for the category '%s' that take less than 2 minutes each. " +
                "Consider that the user already has these habits: %s. " +
                "Respond with just a numbered list of habit suggestions, each with a brief description.",
                category, existingHabitsText);

        String aiResponse = callOpenAI(prompt);
        List<String> strings1 = new java.util.ArrayList<>(java.util.Arrays.asList(aiResponse.split("\n")));
        List<String> strings = new java.util.ArrayList<>(strings1);
        return strings;
    }

    private String buildPrompt(AIMessageRequest request, User user) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("You are a supportive and encouraging habit coach. ");

        switch (request.getContextType()) {
            case "motivation":
                prompt.append("Provide a short, motivational message (max 50 words) to encourage the user. ");
                break;
            case "advice":
                prompt.append("Give practical advice about building habits. Keep it concise and actionable. ");
                break;
            case "celebration":
                prompt.append("Celebrate the user's achievement with enthusiasm! Keep it brief and positive. ");
                break;
            default:
                prompt.append("Have a friendly conversation about habits and personal development. ");
        }

        prompt.append("User's message: ").append(request.getMessage());
        return prompt.toString();
    }

    private String buildMotivationalPrompt(Habit habit) {
        long streak = habit.getCurrentStreak();
        boolean completedToday = habit.isCompletedToday();

        String prompt = String.format(
                "You are a supportive habit coach. Generate a short motivational message (max 40 words) for a user " +
                "working on the habit '%s' in category '%s'. Their current streak is %d days. " +
                "They %s completed it today. Be encouraging and specific to their progress.",
                habit.getTitle(),
                habit.getCategory(),
                streak,
                completedToday ? "have" : "haven't"
        );

        return prompt;
    }

    private String callOpenAI(String prompt) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + openAiApiKey);
            headers.set("Content-Type", "application/json");

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "gpt-3.5-turbo");
            List<Map<String, String>> value = new java.util.ArrayList<>();
            Map<String, String> e = new HashMap<>();
            e.put("role", "user");
            e.put("content", prompt);
            value.add(e);
            requestBody.put("messages", value);
            requestBody.put("max_tokens", 150);
            requestBody.put("temperature", 0.7);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    OPENAI_API_URL, HttpMethod.POST, entity, String.class);

            // Simple fallback approach - in a real app, you'd parse JSON properly
            String responseBody = response.getBody();
            if (responseBody != null && responseBody.contains("content")) {
                // Extract content between quotes after "content":
                int contentStart = responseBody.indexOf("\"content\":\"") + 11;
                int contentEnd = responseBody.indexOf("\"", contentStart);
                if (contentStart > 10 && contentEnd > contentStart) {
                    return responseBody.substring(contentStart, contentEnd);
                }
            }

            return "I'm here to help you with your habits! Keep up the great work! 🌟";
        } catch (Exception e) {
            // Fallback response if OpenAI API fails
            return getFallbackResponse(prompt);
        }
    }

    private String getFallbackResponse(String prompt) {
        if (prompt.contains("motivation")) {
            return "You're doing amazing! Every small step counts towards building lasting habits. Keep going! 💪";
        } else if (prompt.contains("celebration")) {
            return "Congratulations! 🎉 You're building an incredible habit. This consistency will pay off!";
        } else if (prompt.contains("advice")) {
            return "Remember: Start small, be consistent, and celebrate every win. You've got this!";
        }
        return "I'm here to support your habit journey. What would you like to work on today?";
    }
}
