package com.opencampus.cartel.dto.request;

import com.opencampus.cartel.model.enums.OperationStatus;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OperationUpdateRequest {
    @Size(min = 3, max = 100, message = "Operation name must be between 3 and 100 characters")
    private String name;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    private OperationStatus status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String location;
    private Integer riskLevel;
    private BigDecimal estimatedProfit;
    private BigDecimal actualProfit;
    private List<Long> participantIds;
}