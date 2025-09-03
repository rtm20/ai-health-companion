package com.habitcompanion.service;

import com.habitcompanion.dto.HabitRequest;
import com.habitcompanion.dto.HabitResponse;
import com.habitcompanion.model.Habit;
import com.habitcompanion.model.HabitCompletion;
import com.habitcompanion.model.User;
import com.habitcompanion.repository.HabitRepository;
import com.habitcompanion.repository.HabitCompletionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class HabitService {

    @Autowired
    private HabitRepository habitRepository;

    @Autowired
    private HabitCompletionRepository habitCompletionRepository;

    @Autowired
    private AuthService authService;

    public List<HabitResponse> getUserHabits() {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("User not authenticated");
        }

        List<Habit> habits = habitRepository.findByUserAndIsActiveTrue(currentUser);
        return habits.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    public HabitResponse createHabit(HabitRequest habitRequest) {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("User not authenticated");
        }

        // Check subscription limits for free users
        if (currentUser.getSubscriptionTier() == User.SubscriptionTier.FREE) {
            long activeHabitsCount = habitRepository.countActiveHabitsByUser(currentUser);
            if (activeHabitsCount >= 2) {
                throw new RuntimeException("Free users can only have 2 active habits. Upgrade to Premium for unlimited habits!");
            }
        }

        Habit habit = new Habit(currentUser, habitRequest.getTitle(), 
                              habitRequest.getDescription(), habitRequest.getCategory());
        habit.setTargetFrequency(habitRequest.getTargetFrequency());
        habit.setReminderTime(habitRequest.getReminderTime());

        habit = habitRepository.save(habit);
        return convertToResponse(habit);
    }

    public HabitResponse updateHabit(UUID habitId, HabitRequest habitRequest) {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("User not authenticated");
        }

        Optional<Habit> habitOpt = habitRepository.findById(habitId);
        if (!habitOpt.isPresent() || !habitOpt.get().getUser().equals(currentUser)) {
            throw new RuntimeException("Habit not found or unauthorized");
        }

        Habit habit = habitOpt.get();
        habit.setTitle(habitRequest.getTitle());
        habit.setDescription(habitRequest.getDescription());
        habit.setCategory(habitRequest.getCategory());
        habit.setTargetFrequency(habitRequest.getTargetFrequency());
        habit.setReminderTime(habitRequest.getReminderTime());

        habit = habitRepository.save(habit);
        return convertToResponse(habit);
    }

    public String deleteHabit(UUID habitId) {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("User not authenticated");
        }

        Optional<Habit> habitOpt = habitRepository.findById(habitId);
        if (!habitOpt.isPresent() || !habitOpt.get().getUser().equals(currentUser)) {
            throw new RuntimeException("Habit not found or unauthorized");
        }

        Habit habit = habitOpt.get();
        habit.setIsActive(false);
        habitRepository.save(habit);
        
        return "Habit deleted successfully";
    }

    public String markHabitComplete(UUID habitId, String notes) {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("User not authenticated");
        }

        Optional<Habit> habitOpt = habitRepository.findById(habitId);
        if (!habitOpt.isPresent() || !habitOpt.get().getUser().equals(currentUser)) {
            throw new RuntimeException("Habit not found or unauthorized");
        }

        Habit habit = habitOpt.get();
        
        // Check if already completed today
        long todayCompletions = habitCompletionRepository.countTodayCompletions(habit);
        if (todayCompletions >= habit.getTargetFrequency()) {
            return "Habit already completed for today!";
        }

        HabitCompletion completion = new HabitCompletion(habit, notes);
        habitCompletionRepository.save(completion);

        return "Habit marked as complete! Great job! 🎉";
    }

    private HabitResponse convertToResponse(Habit habit) {
        long totalCompletions = habit.getCompletions().size();
        long currentStreak = calculateCurrentStreak(habit);
        boolean completedToday = habit.isCompletedToday();

        return new HabitResponse(
                habit.getId().toString(),
                habit.getTitle(),
                habit.getDescription(),
                habit.getCategory(),
                habit.getTargetFrequency(),
                habit.getReminderTime(),
                habit.getIsActive(),
                habit.getCreatedAt(),
                currentStreak,
                completedToday,
                totalCompletions
        );
    }

    private long calculateCurrentStreak(Habit habit) {
        List<HabitCompletion> recentCompletions = habitCompletionRepository
                .findByHabitOrderByCompletedAtDesc(habit);
        
        if (recentCompletions.isEmpty()) {
            return 0;
        }

        long streak = 0;
        LocalDateTime currentDate = LocalDateTime.now().toLocalDate().atStartOfDay();
        
        for (HabitCompletion completion : recentCompletions) {
            LocalDateTime completionDate = completion.getCompletedAt().toLocalDate().atStartOfDay();
            
            if (completionDate.equals(currentDate) || completionDate.equals(currentDate.minusDays(1))) {
                streak++;
                currentDate = currentDate.minusDays(1);
            } else {
                break;
            }
        }
        
        return streak;
    }
}
