package com.opencampus.cartel.controllers;

import com.opencampus.cartel.dto.request.WarehouseCreateRequest;
import com.opencampus.cartel.dto.request.WarehouseUpdateRequest;
import com.opencampus.cartel.dto.response.WarehouseResponse;
import com.opencampus.cartel.services.WarehouseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/warehouses")
@Tag(name = "Warehouses", description = "Warehouse management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class WarehouseController {

    @Autowired
    private WarehouseService warehouseService;

    @PostMapping
    @Operation(summary = "Create a new warehouse", description = "Add a new warehouse to the network. Admin and Boss only.")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BOSS')")
    public ResponseEntity<?> createWarehouse(@Valid @RequestBody WarehouseCreateRequest request) {
        try {
            WarehouseResponse response = warehouseService.createWarehouse(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get warehouse by ID")
    public ResponseEntity<?> getWarehouseById(@PathVariable Long id) {
        try {
            WarehouseResponse response = warehouseService.getWarehouseById(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    @Operation(summary = "Get all warehouses")
    public ResponseEntity<Page<WarehouseResponse>> getAllWarehouses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return ResponseEntity.ok(warehouseService.getAllWarehouses(pageable));
    }

    @GetMapping("/city/{city}")
    @Operation(summary = "Get warehouses by city")
    public ResponseEntity<List<WarehouseResponse>> getWarehousesByCity(@PathVariable String city) {
        return ResponseEntity.ok(warehouseService.getWarehousesByCity(city));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update warehouse")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BOSS')")
    public ResponseEntity<?> updateWarehouse(@PathVariable Long id, @Valid @RequestBody WarehouseUpdateRequest request) {
        try {
            WarehouseResponse response = warehouseService.updateWarehouse(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}