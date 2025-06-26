package com.opencampus.cartel.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageResponse {
    private Long id;
    private UserResponse sender;
    private UserResponse receiver;
    private String subject;
    private String content;
    private Boolean isEncrypted;
    private Boolean isRead;
    private Integer priorityLevel;
    private String messageType;
    private LocalDateTime sentAt;
    private LocalDateTime readAt;
}