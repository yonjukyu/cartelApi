package com.opencampus.cartel.services;


import com.opencampus.cartel.dto.request.WarehouseCreateRequest;
import com.opencampus.cartel.dto.request.WarehouseUpdateRequest;
import com.opencampus.cartel.dto.response.WarehouseResponse;
import com.opencampus.cartel.model.entity.User;
import com.opencampus.cartel.model.entity.Warehouse;
import com.opencampus.cartel.repository.UserRepository;
import com.opencampus.cartel.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WarehouseService {

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    public WarehouseResponse createWarehouse(WarehouseCreateRequest request) {
        if (warehouseRepository.existsByCodeName(request.getCodeName())) {
            throw new RuntimeException("Warehouse code name already exists");
        }

        Warehouse warehouse = new Warehouse();
        warehouse.setName(request.getName());
        warehouse.setCodeName(request.getCodeName());
        warehouse.setAddress(request.getAddress());
        warehouse.setCity(request.getCity());
        warehouse.setCountry(request.getCountry());
        warehouse.setCoordinates(request.getCoordinates());
        warehouse.setCapacity(request.getCapacity());
        warehouse.setSecurityLevel(request.getSecurityLevel());
        warehouse.setIsActive(true);

        if (request.getManagerId() != null) {
            User manager = userRepository.findById(request.getManagerId())
                    .orElseThrow(() -> new RuntimeException("Manager not found"));
            warehouse.setManager(manager);
        }

        Warehouse savedWarehouse = warehouseRepository.save(warehouse);
        return convertToResponse(savedWarehouse);
    }

    public WarehouseResponse getWarehouseById(Long id) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Warehouse not found"));
        return convertToResponse(warehouse);
    }

    public Page<WarehouseResponse> getAllWarehouses(Pageable pageable) {
        return warehouseRepository.findByIsActiveTrue(pageable).map(this::convertToResponse);
    }

    public List<WarehouseResponse> getWarehousesByCity(String city) {
        return warehouseRepository.findActiveWarehousesByCity(city)
                .stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    public WarehouseResponse updateWarehouse(Long id, WarehouseUpdateRequest request) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Warehouse not found"));

        if (request.getName() != null) warehouse.setName(request.getName());
        if (request.getAddress() != null) warehouse.setAddress(request.getAddress());
        if (request.getCity() != null) warehouse.setCity(request.getCity());
        if (request.getCountry() != null) warehouse.setCountry(request.getCountry());
        if (request.getCoordinates() != null) warehouse.setCoordinates(request.getCoordinates());
        if (request.getCapacity() != null) warehouse.setCapacity(request.getCapacity());
        if (request.getSecurityLevel() != null) warehouse.setSecurityLevel(request.getSecurityLevel());
        if (request.getIsActive() != null) warehouse.setIsActive(request.getIsActive());

        if (request.getManagerId() != null) {
            User manager = userRepository.findById(request.getManagerId())
                    .orElseThrow(() -> new RuntimeException("Manager not found"));
            warehouse.setManager(manager);
        }

        return convertToResponse(warehouseRepository.save(warehouse));
    }

    WarehouseResponse convertToResponse(Warehouse warehouse) {
        WarehouseResponse response = new WarehouseResponse();
        response.setId(warehouse.getId());
        response.setName(warehouse.getName());
        response.setCodeName(warehouse.getCodeName());
        response.setAddress(warehouse.getAddress());
        response.setCity(warehouse.getCity());
        response.setCountry(warehouse.getCountry());
        response.setCoordinates(warehouse.getCoordinates());
        response.setCapacity(warehouse.getCapacity());
        response.setSecurityLevel(warehouse.getSecurityLevel());
        response.setIsActive(warehouse.getIsActive());
        response.setCreatedAt(warehouse.getCreatedAt());
        response.setUpdatedAt(warehouse.getUpdatedAt());

        if (warehouse.getManager() != null) {
            response.setManager(userService.convertToResponse(warehouse.getManager()));
        }

        return response;
    }
}