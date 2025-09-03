package com.habitcompanion.repository;

import com.habitcompanion.model.Habit;
import com.habitcompanion.model.HabitCompletion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface HabitCompletionRepository extends JpaRepository<HabitCompletion, UUID> {
    List<HabitCompletion> findByHabitOrderByCompletedAtDesc(Habit habit);
    
    @Query("SELECT hc FROM HabitCompletion hc WHERE hc.habit = :habit AND hc.completedAt >= :startDate AND hc.completedAt <= :endDate")
    List<HabitCompletion> findByHabitAndDateRange(@Param("habit") Habit habit, 
                                                  @Param("startDate") LocalDateTime startDate, 
                                                  @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COUNT(hc) FROM HabitCompletion hc WHERE hc.habit = :habit AND DATE(hc.completedAt) = CURRENT_DATE")
    long countTodayCompletions(@Param("habit") Habit habit);
    
    @Query("SELECT hc FROM HabitCompletion hc WHERE hc.habit.user.id = :userId AND hc.completedAt >= :startDate")
    List<HabitCompletion> findRecentCompletionsByUser(@Param("userId") UUID userId, @Param("startDate") LocalDateTime startDate);
}
