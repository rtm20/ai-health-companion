package com.habitcompanion.controller;

import com.habitcompanion.dto.HabitRequest;
import com.habitcompanion.dto.HabitResponse;
import com.habitcompanion.service.HabitService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/habits")
public class HabitController {

    @Autowired
    private HabitService habitService;

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok("Habits API is working!");
    }

    @GetMapping
    public ResponseEntity<List<HabitResponse>> getUserHabits() {
        try {
            List<HabitResponse> habits = habitService.getUserHabits();
            return ResponseEntity.ok(habits);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createHabit(@Valid @RequestBody HabitRequest habitRequest) {
        try {
            HabitResponse habit = habitService.createHabit(habitRequest);
            return ResponseEntity.ok(habit);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{habitId}")
    public ResponseEntity<?> updateHabit(@PathVariable UUID habitId, 
                                        @Valid @RequestBody HabitRequest habitRequest) {
        try {
            HabitResponse habit = habitService.updateHabit(habitId, habitRequest);
            return ResponseEntity.ok(habit);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{habitId}")
    public ResponseEntity<?> deleteHabit(@PathVariable UUID habitId) {
        try {
            String result = habitService.deleteHabit(habitId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{habitId}/complete")
    public ResponseEntity<?> markHabitComplete(@PathVariable UUID habitId, 
                                              @RequestParam(required = false) String notes) {
        try {
            String result = habitService.markHabitComplete(habitId, notes);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
