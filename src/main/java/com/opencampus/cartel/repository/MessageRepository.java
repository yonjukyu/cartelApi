package com.opencampus.cartel.repository;

import com.opencampus.cartel.model.entity.Message;
import com.opencampus.cartel.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findBySender(User sender);

    List<Message> findByReceiver(User receiver);

    Page<Message> findBySender(User sender, Pageable pageable);

    Page<Message> findByReceiver(User receiver, Pageable pageable);

    @Query("SELECT m FROM Message m WHERE m.receiver = :user AND m.isRead = false ORDER BY m.sentAt DESC")
    List<Message> findUnreadMessagesByUser(@Param("user") User user);

    @Query("SELECT COUNT(m) FROM Message m WHERE m.receiver = :user AND m.isRead = false")
    Long countUnreadMessagesByUser(@Param("user") User user);

    @Query("SELECT m FROM Message m WHERE (m.sender = :user1 AND m.receiver = :user2) OR (m.sender = :user2 AND m.receiver = :user1) ORDER BY m.sentAt ASC")
    List<Message> findConversationBetweenUsers(@Param("user1") User user1, @Param("user2") User user2);

    @Query("SELECT m FROM Message m WHERE m.receiver = :user AND m.sentAt BETWEEN :startDate AND :endDate")
    List<Message> findMessagesByReceiverAndDateRange(@Param("user") User user, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT m FROM Message m WHERE m.priorityLevel >= :minPriority AND m.receiver = :user AND m.isRead = false")
    List<Message> findHighPriorityUnreadMessages(@Param("user") User user, @Param("minPriority") Integer minPriority);

    @Query("SELECT m FROM Message m WHERE m.subject LIKE %:keyword% OR m.content LIKE %:keyword%")
    Page<Message> searchMessages(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT m FROM Message m WHERE m.messageType = :messageType AND m.receiver = :user")
    List<Message> findByMessageTypeAndReceiver(@Param("messageType") String messageType, @Param("user") User user);
}