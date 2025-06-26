package com.opencampus.cartel.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionCreateRequest {
    @NotNull(message = "Operation ID is required")
    private Long operationId;

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Buyer ID is required")
    private Long buyerId;

    @NotNull(message = "Seller ID is required")
    private Long sellerId;

    @Min(value = 1, message = "Quantity must be at least 1")
    @NotNull(message = "Quantity is required")
    private Integer quantity;

    @DecimalMin(value = "0.0", inclusive = false, message = "Unit price must be greater than 0")
    @NotNull(message = "Unit price is required")
    private BigDecimal unitPrice;

    private String transactionLocation;

    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    private String notes;
}
