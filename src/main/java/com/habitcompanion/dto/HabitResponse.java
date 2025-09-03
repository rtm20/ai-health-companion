package com.habitcompanion.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class HabitResponse {
    private String id;
    private String title;
    private String description;
    private String category;
    private Integer targetFrequency;
    private LocalTime reminderTime;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private long currentStreak;
    private boolean completedToday;
    private long totalCompletions;

    // Constructors
    public HabitResponse() {}

    public HabitResponse(String id, String title, String description, String category,
                        Integer targetFrequency, LocalTime reminderTime, Boolean isActive,
                        LocalDateTime createdAt, long currentStreak, boolean completedToday, long totalCompletions) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.targetFrequency = targetFrequency;
        this.reminderTime = reminderTime;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.currentStreak = currentStreak;
        this.completedToday = completedToday;
        this.totalCompletions = totalCompletions;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Integer getTargetFrequency() { return targetFrequency; }
    public void setTargetFrequency(Integer targetFrequency) { this.targetFrequency = targetFrequency; }

    public LocalTime getReminderTime() { return reminderTime; }
    public void setReminderTime(LocalTime reminderTime) { this.reminderTime = reminderTime; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public long getCurrentStreak() { return currentStreak; }
    public void setCurrentStreak(long currentStreak) { this.currentStreak = currentStreak; }

    public boolean isCompletedToday() { return completedToday; }
    public void setCompletedToday(boolean completedToday) { this.completedToday = completedToday; }

    public long getTotalCompletions() { return totalCompletions; }
    public void setTotalCompletions(long totalCompletions) { this.totalCompletions = totalCompletions; }
}
