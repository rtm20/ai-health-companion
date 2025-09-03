package com.habitcompanion.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "habits")
public class Habit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotBlank
    @Size(max = 255)
    private String title;

    @Size(max = 1000)
    private String description;

    @Size(max = 100)
    private String category;

    @Column(name = "target_frequency")
    private Integer targetFrequency = 1; // times per day

    @Column(name = "reminder_time")
    private LocalTime reminderTime;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "habit", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<HabitCompletion> completions = new HashSet<>();

    // Constructors
    public Habit() {}

    public Habit(User user, String title, String description, String category) {
        this.user = user;
        this.title = title;
        this.description = description;
        this.category = category;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

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

    public Set<HabitCompletion> getCompletions() { return completions; }
    public void setCompletions(Set<HabitCompletion> completions) { this.completions = completions; }

    // Business methods
    public long getCurrentStreak() {
        // Calculate current streak based on completions
        if (completions.isEmpty()) return 0;
        
        // Implementation for streak calculation
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1).toLocalDate().atStartOfDay();
        return completions.stream()
                .filter(c -> c.getCompletedAt().isAfter(yesterday))
                .count();
    }

    public boolean isCompletedToday() {
        LocalDateTime today = LocalDateTime.now().toLocalDate().atStartOfDay();
        return completions.stream()
                .anyMatch(c -> c.getCompletedAt().isAfter(today));
    }
}
