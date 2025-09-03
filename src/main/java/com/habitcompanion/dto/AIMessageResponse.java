package com.habitcompanion.dto;

import java.time.LocalDateTime;

public class AIMessageResponse {
    private String response;
    private String contextType;
    private LocalDateTime timestamp;

    // Constructors
    public AIMessageResponse() {}

    public AIMessageResponse(String response, String contextType) {
        this.response = response;
        this.contextType = contextType;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public String getResponse() { return response; }
    public void setResponse(String response) { this.response = response; }

    public String getContextType() { return contextType; }
    public void setContextType(String contextType) { this.contextType = contextType; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
