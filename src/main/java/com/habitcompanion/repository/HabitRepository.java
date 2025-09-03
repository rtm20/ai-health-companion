package com.habitcompanion.repository;

import com.habitcompanion.model.Habit;
import com.habitcompanion.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HabitRepository extends JpaRepository<Habit, UUID> {
    List<Habit> findByUserAndIsActiveTrue(User user);
    List<Habit> findByUser(User user);
    
    @Query("SELECT COUNT(h) FROM Habit h WHERE h.user = :user AND h.isActive = true")
    long countActiveHabitsByUser(@Param("user") User user);
    
    @Query("SELECT h FROM Habit h WHERE h.user = :user AND h.category = :category AND h.isActive = true")
    List<Habit> findActiveHabitsByUserAndCategory(@Param("user") User user, @Param("category") String category);
}
