package com.opencampus.cartel.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionResponse {
    private Long id;
    private OperationResponse operation;
    private ProductResponse product;
    private UserResponse buyer;
    private UserResponse seller;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalAmount;
    private String transactionLocation;
    private String notes;
    private LocalDateTime transactionDate;
}