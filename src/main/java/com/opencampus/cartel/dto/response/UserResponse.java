package com.opencampus.cartel.dto.response;

import com.opencampus.cartel.model.enums.Role;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String codeName;
    private Role role;
    private String phoneNumber;
    private String territory;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}