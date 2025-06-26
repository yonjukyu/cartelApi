package com.opencampus.cartel.dto.response;

import com.opencampus.cartel.model.enums.OperationStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OperationResponse {
    private Long id;
    private String name;
    private String codeName;
    private String description;
    private UserResponse leader;
    private OperationStatus status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String location;
    private Integer riskLevel;
    private BigDecimal estimatedProfit;
    private BigDecimal actualProfit;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<UserResponse> participants;
}