package com.opencampus.cartel.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class WarehouseCreateRequest {
    @NotBlank(message = "Warehouse name is required")
    @Size(min = 3, max = 100, message = "Warehouse name must be between 3 and 100 characters")
    private String name;

    @NotBlank(message = "Code name is required")
    @Size(min = 3, max = 50, message = "Code name must be between 3 and 50 characters")
    private String codeName;

    @Size(max = 500, message = "Address cannot exceed 500 characters")
    private String address;

    private String city;
    private String country;
    private String coordinates;
    private Integer capacity;
    private Integer securityLevel;
    private Long managerId;
}