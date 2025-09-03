package com.habitcompanion.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalTime;

public class HabitRequest {
    @NotBlank
    @Size(max = 255)
    private String title;

    @Size(max = 1000)
    private String description;

    @Size(max = 100)
    private String category;

    private Integer targetFrequency = 1;

    private LocalTime reminderTime;

    // Constructors
    public HabitRequest() {}

    public HabitRequest(String title, String description, String category) {
        this.title = title;
        this.description = description;
        this.category = category;
    }

    // Getters and Setters
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
}
