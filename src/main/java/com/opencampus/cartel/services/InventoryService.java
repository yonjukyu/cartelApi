package com.opencampus.cartel.services;


import com.opencampus.cartel.dto.request.InventoryCreateRequest;
import com.opencampus.cartel.dto.request.InventoryUpdateRequest;
import com.opencampus.cartel.dto.response.InventoryResponse;
import com.opencampus.cartel.model.entity.Inventory;
import com.opencampus.cartel.model.entity.Product;
import com.opencampus.cartel.model.entity.Warehouse;
import com.opencampus.cartel.repository.InventoryRepository;
import com.opencampus.cartel.repository.ProductRepository;
import com.opencampus.cartel.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private WarehouseService warehouseService;

    public InventoryResponse createInventory(InventoryCreateRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                .orElseThrow(() -> new RuntimeException("Warehouse not found"));

        // Check if inventory already exists for this product in this warehouse
        if (inventoryRepository.findByProductAndWarehouse(product, warehouse).isPresent()) {
            throw new RuntimeException("Inventory already exists for this product in this warehouse");
        }

        Inventory inventory = new Inventory();
        inventory.setProduct(product);
        inventory.setWarehouse(warehouse);
        inventory.setQuantity(request.getQuantity());
        inventory.setReservedQuantity(request.getReservedQuantity());
        inventory.setMinimumStockLevel(request.getMinimumStockLevel());
        inventory.setLastRestocked(LocalDateTime.now());

        return convertToResponse(inventoryRepository.save(inventory));
    }

    public InventoryResponse getInventoryById(Long id) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));
        return convertToResponse(inventory);
    }

    public Page<InventoryResponse> getAllInventory(Pageable pageable) {
        return inventoryRepository.findAll(pageable).map(this::convertToResponse);
    }

    public List<InventoryResponse> getAvailableInventory() {
        return inventoryRepository.findAvailableInventory()
                .stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    public List<InventoryResponse> getLowStockInventory() {
        return inventoryRepository.findLowStockInventory()
                .stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    public InventoryResponse updateInventory(Long id, InventoryUpdateRequest request) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        if (request.getQuantity() != null) {
            inventory.setQuantity(request.getQuantity());
            inventory.setLastRestocked(LocalDateTime.now());
        }
        if (request.getReservedQuantity() != null) {
            inventory.setReservedQuantity(request.getReservedQuantity());
        }
        if (request.getMinimumStockLevel() != null) {
            inventory.setMinimumStockLevel(request.getMinimumStockLevel());
        }

        return convertToResponse(inventoryRepository.save(inventory));
    }

    private InventoryResponse convertToResponse(Inventory inventory) {
        InventoryResponse response = new InventoryResponse();
        response.setId(inventory.getId());
        response.setProduct(productService.convertToResponse(inventory.getProduct()));
        response.setWarehouse(warehouseService.convertToResponse(inventory.getWarehouse()));
        response.setQuantity(inventory.getQuantity());
        response.setReservedQuantity(inventory.getReservedQuantity());
        response.setMinimumStockLevel(inventory.getMinimumStockLevel());
        response.setLastRestocked(inventory.getLastRestocked());
        response.setCreatedAt(inventory.getCreatedAt());
        response.setUpdatedAt(inventory.getUpdatedAt());
        return response;
    }
}