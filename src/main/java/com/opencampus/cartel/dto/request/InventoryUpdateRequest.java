package com.opencampus.cartel.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class InventoryUpdateRequest {
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;

    @Min(value = 0, message = "Reserved quantity cannot be negative")
    private Integer reservedQuantity;

    private Integer minimumStockLevel;
}