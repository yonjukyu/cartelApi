package com.opencampus.cartel.controllers;


import com.opencampus.cartel.dto.request.OperationCreateRequest;
import com.opencampus.cartel.dto.request.OperationUpdateRequest;
import com.opencampus.cartel.dto.response.OperationResponse;
import com.opencampus.cartel.model.enums.OperationStatus;
import com.opencampus.cartel.services.OperationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/operations")
@Tag(name = "Operations", description = "Operation management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class OperationController {

    @Autowired
    private OperationService operationService;

    @PostMapping
    @Operation(
            summary = "Create a new operation",
            description = "Plan a new cartel operation. Boss and Lieutenant only."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operation created successfully",
                    content = @Content(schema = @Schema(implementation = OperationResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('BOSS') or hasRole('LIEUTENANT')")
    public ResponseEntity<?> createOperation(@Valid @RequestBody OperationCreateRequest request) {
        try {
            OperationResponse response = operationService.createOperation(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get operation by ID",
            description = "Retrieve operation details by ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operation found",
                    content = @Content(schema = @Schema(implementation = OperationResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Operation not found")
    })
    public ResponseEntity<?> getOperationById(
            @Parameter(description = "Operation ID", required = true)
            @PathVariable Long id) {
        try {
            OperationResponse response = operationService.getOperationById(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    @Operation(
            summary = "Get all operations",
            description = "Retrieve all operations with pagination"
    )
    public ResponseEntity<Page<OperationResponse>> getAllOperations(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field", example = "createdAt")
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction", example = "desc")
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<OperationResponse> operations = operationService.getAllOperations(pageable);
        return ResponseEntity.ok(operations);
    }

    @GetMapping("/leader/{leaderId}")
    @Operation(
            summary = "Get operations by leader",
            description = "Retrieve operations led by a specific user"
    )
    public ResponseEntity<Page<OperationResponse>> getOperationsByLeader(
            @Parameter(description = "Leader user ID", required = true)
            @PathVariable Long leaderId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<OperationResponse> operations = operationService.getOperationsByLeader(leaderId, pageable);
        return ResponseEntity.ok(operations);
    }

    @GetMapping("/status/{status}")
    @Operation(
            summary = "Get operations by status",
            description = "Retrieve operations filtered by status"
    )
    public ResponseEntity<Page<OperationResponse>> getOperationsByStatus(
            @Parameter(description = "Operation status", required = true)
            @PathVariable OperationStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<OperationResponse> operations = operationService.getOperationsByStatus(status, pageable);
        return ResponseEntity.ok(operations);
    }

    @GetMapping("/participant/{userId}")
    @Operation(
            summary = "Get operations by participant",
            description = "Retrieve operations where a user is a participant"
    )
    public ResponseEntity<List<OperationResponse>> getOperationsByParticipant(
            @Parameter(description = "Participant user ID", required = true)
            @PathVariable Long userId) {

        List<OperationResponse> operations = operationService.getOperationsByParticipant(userId);
        return ResponseEntity.ok(operations);
    }

    @GetMapping("/date-range")
    @Operation(
            summary = "Get operations by date range",
            description = "Retrieve operations within a specific date range"
    )
    public ResponseEntity<List<OperationResponse>> getOperationsByDateRange(
            @Parameter(description = "Start date (yyyy-MM-dd'T'HH:mm:ss)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date (yyyy-MM-dd'T'HH:mm:ss)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        List<OperationResponse> operations = operationService.getOperationsByDateRange(startDate, endDate);
        return ResponseEntity.ok(operations);
    }

    @GetMapping("/location/{location}")
    @Operation(
            summary = "Get active operations by location",
            description = "Retrieve active operations in a specific location"
    )
    public ResponseEntity<List<OperationResponse>> getActiveOperationsByLocation(
            @Parameter(description = "Location name", required = true)
            @PathVariable String location) {

        List<OperationResponse> operations = operationService.getActiveOperationsByLocation(location);
        return ResponseEntity.ok(operations);
    }

    @GetMapping("/high-risk/{minRisk}")
    @Operation(
            summary = "Get high-risk operations",
            description = "Retrieve operations with risk level above minimum threshold"
    )
    @PreAuthorize("hasRole('ADMIN') or hasRole('BOSS')")
    public ResponseEntity<List<OperationResponse>> getHighRiskOperations(
            @Parameter(description = "Minimum risk level (1-10)", required = true)
            @PathVariable Integer minRisk) {

        List<OperationResponse> operations = operationService.getHighRiskOperations(minRisk);
        return ResponseEntity.ok(operations);
    }

    @GetMapping("/search")
    @Operation(
            summary = "Search operations",
            description = "Search operations by name or code name"
    )
    public ResponseEntity<Page<OperationResponse>> searchOperations(
            @Parameter(description = "Search term", required = true)
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<OperationResponse> operations = operationService.searchOperations(searchTerm, pageable);
        return ResponseEntity.ok(operations);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update operation",
            description = "Update operation details. Boss, Lieutenant, or operation leader only."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operation updated successfully",
                    content = @Content(schema = @Schema(implementation = OperationResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Operation not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('BOSS') or hasRole('LIEUTENANT')")
    public ResponseEntity<?> updateOperation(
            @Parameter(description = "Operation ID", required = true)
            @PathVariable Long id,
            @Valid @RequestBody OperationUpdateRequest request) {
        try {
            OperationResponse response = operationService.updateOperation(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Cancel operation",
            description = "Cancel an operation (sets status to CANCELLED). Boss and admin only."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation cancelled successfully"),
            @ApiResponse(responseCode = "404", description = "Operation not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('BOSS')")
    public ResponseEntity<?> deleteOperation(
            @Parameter(description = "Operation ID", required = true)
            @PathVariable Long id) {
        try {
            operationService.deleteOperation(id);
            return ResponseEntity.ok(Map.of("message", "Operation cancelled successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}