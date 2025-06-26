package com.opencampus.cartel.controllers;

import com.opencampus.cartel.dto.request.InventoryCreateRequest;
import com.opencampus.cartel.dto.request.InventoryUpdateRequest;
import com.opencampus.cartel.dto.response.InventoryResponse;
import com.opencampus.cartel.services.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inventory")
@Tag(name = "Inventory", description = "Inventory management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @PostMapping
    @Operation(summary = "Create inventory entry", description = "Add product to warehouse inventory")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BOSS') or hasRole('LIEUTENANT')")
    public ResponseEntity<?> createInventory(@Valid @RequestBody InventoryCreateRequest request) {
        try {
            InventoryResponse response = inventoryService.createInventory(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get inventory by ID")
    public ResponseEntity<?> getInventoryById(@PathVariable Long id) {
        try {
            InventoryResponse response = inventoryService.getInventoryById(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    @Operation(summary = "Get all inventory")
    public ResponseEntity<Page<InventoryResponse>> getAllInventory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(inventoryService.getAllInventory(pageable));
    }

    @GetMapping("/available")
    @Operation(summary = "Get available inventory", description = "Get inventory with quantity > 0")
    public ResponseEntity<List<InventoryResponse>> getAvailableInventory() {
        return ResponseEntity.ok(inventoryService.getAvailableInventory());
    }

    @GetMapping("/low-stock")
    @Operation(summary = "Get low stock alerts", description = "Get inventory below minimum stock level")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BOSS') or hasRole('LIEUTENANT')")
    public ResponseEntity<List<InventoryResponse>> getLowStockInventory() {
        return ResponseEntity.ok(inventoryService.getLowStockInventory());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update inventory")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BOSS') or hasRole('LIEUTENANT')")
    public ResponseEntity<?> updateInventory(@PathVariable Long id, @Valid @RequestBody InventoryUpdateRequest request) {
        try {
            InventoryResponse response = inventoryService.updateInventory(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}