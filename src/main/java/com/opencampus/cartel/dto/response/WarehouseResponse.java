package com.opencampus.cartel.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WarehouseResponse {
    private Long id;
    private String name;
    private String codeName;
    private String address;
    private String city;
    private String country;
    private String coordinates;
    private Integer capacity;
    private Integer securityLevel;
    private Boolean isActive;
    private UserResponse manager;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}