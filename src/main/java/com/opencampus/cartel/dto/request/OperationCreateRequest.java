package com.opencampus.cartel.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OperationCreateRequest {
    @NotBlank(message = "Operation name is required")
    @Size(min = 3, max = 100, message = "Operation name must be between 3 and 100 characters")
    private String name;

    @NotBlank(message = "Code name is required")
    @Size(min = 3, max = 50, message = "Code name must be between 3 and 50 characters")
    private String codeName;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @NotNull(message = "Leader ID is required")
    private Long leaderId;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String location;
    private Integer riskLevel;
    private BigDecimal estimatedProfit;
    private List<Long> participantIds;
}