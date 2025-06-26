package com.opencampus.cartel.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InventoryCreateRequest {
    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Warehouse ID is required")
    private Long warehouseId;

    @Min(value = 0, message = "Quantity cannot be negative")
    @NotNull(message = "Quantity is required")
    private Integer quantity;

    @Min(value = 0, message = "Reserved quantity cannot be negative")
    private Integer reservedQuantity = 0;

    private Integer minimumStockLevel = 0;
}