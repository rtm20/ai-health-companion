package com.habitcompanion.dto;

import javax.validation.constraints.NotBlank;

public class AIMessageRequest {
    @NotBlank
    private String message;
    
    private String contextType = "general"; // motivation, advice, celebration, general
    
    private String habitId; // optional, for habit-specific context

    // Constructors
    public AIMessageRequest() {}

    public AIMessageRequest(String message, String contextType) {
        this.message = message;
        this.contextType = contextType;
    }

    // Getters and Setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getContextType() { return contextType; }
    public void setContextType(String contextType) { this.contextType = contextType; }

    public String getHabitId() { return habitId; }
    public void setHabitId(String habitId) { this.habitId = habitId; }
}
