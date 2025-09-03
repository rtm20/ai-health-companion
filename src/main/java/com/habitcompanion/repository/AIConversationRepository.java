package com.habitcompanion.repository;

import com.habitcompanion.model.AIConversation;
import com.habitcompanion.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AIConversationRepository extends JpaRepository<AIConversation, UUID> {
    List<AIConversation> findByUserOrderByCreatedAtDesc(User user);
    
    @Query("SELECT ac FROM AIConversation ac WHERE ac.user = :user AND ac.createdAt >= :startDate ORDER BY ac.createdAt DESC")
    List<AIConversation> findRecentConversations(@Param("user") User user, @Param("startDate") LocalDateTime startDate);
    
    @Query("SELECT ac FROM AIConversation ac WHERE ac.user = :user AND ac.contextType = :contextType ORDER BY ac.createdAt DESC")
    List<AIConversation> findByUserAndContextType(@Param("user") User user, @Param("contextType") String contextType);
}
