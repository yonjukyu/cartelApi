package com.opencampus.cartel.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class WarehouseUpdateRequest {
    @Size(min = 3, max = 100, message = "Warehouse name must be between 3 and 100 characters")
    private String name;

    @Size(max = 500, message = "Address cannot exceed 500 characters")
    private String address;

    private String city;
    private String country;
    private String coordinates;
    private Integer capacity;
    private Integer securityLevel;
    private Long managerId;
    private Boolean isActive;
}