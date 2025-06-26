package com.opencampus.cartel.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InventoryResponse {
    private Long id;
    private ProductResponse product;
    private WarehouseResponse warehouse;
    private Integer quantity;
    private Integer reservedQuantity;
    private Integer minimumStockLevel;
    private LocalDateTime lastRestocked;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
