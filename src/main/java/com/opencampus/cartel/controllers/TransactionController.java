package com.opencampus.cartel.controllers;

import com.opencampus.cartel.dto.request.TransactionCreateRequest;
import com.opencampus.cartel.dto.response.TransactionResponse;
import com.opencampus.cartel.services.TransactionService;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Transactions", description = "Transaction management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    @Operation(
            summary = "Create a new transaction",
            description = "Record a new transaction between cartel members"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Transaction created successfully",
                    content = @Content(schema = @Schema(implementation = TransactionResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('BOSS') or hasRole('LIEUTENANT')")
    public ResponseEntity<?> createTransaction(@Valid @RequestBody TransactionCreateRequest request) {
        try {
            TransactionResponse response = transactionService.createTransaction(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get transaction by ID",
            description = "Retrieve transaction details by ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Transaction found",
                    content = @Content(schema = @Schema(implementation = TransactionResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    public ResponseEntity<?> getTransactionById(
            @Parameter(description = "Transaction ID", required = true)
            @PathVariable Long id) {
        try {
            TransactionResponse response = transactionService.getTransactionById(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    @Operation(
            summary = "Get all transactions",
            description = "Retrieve all transactions with pagination. Admin and Boss only."
    )
    @PreAuthorize("hasRole('ADMIN') or hasRole('BOSS')")
    public ResponseEntity<Page<TransactionResponse>> getAllTransactions(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field", example = "transactionDate")
            @RequestParam(defaultValue = "transactionDate") String sortBy,
            @Parameter(description = "Sort direction", example = "desc")
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<TransactionResponse> transactions = transactionService.getAllTransactions(pageable);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/user/{userId}")
    @Operation(
            summary = "Get transactions by user",
            description = "Retrieve transactions involving a specific user (as buyer or seller)"
    )
    @PreAuthorize("hasRole('ADMIN') or hasRole('BOSS') or hasRole('LIEUTENANT')")
    public ResponseEntity<Page<TransactionResponse>> getTransactionsByUser(
            @Parameter(description = "User ID", required = true)
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("transactionDate").descending());
        Page<TransactionResponse> transactions = transactionService.getTransactionsByUser(userId, pageable);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/date-range")
    @Operation(
            summary = "Get transactions by date range",
            description = "Retrieve transactions within a specific date range"
    )
    @PreAuthorize("hasRole('ADMIN') or hasRole('BOSS')")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByDateRange(
            @Parameter(description = "Start date (yyyy-MM-dd'T'HH:mm:ss)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date (yyyy-MM-dd'T'HH:mm:ss)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        List<TransactionResponse> transactions = transactionService.getTransactionsByDateRange(startDate, endDate);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/sales-report/{sellerId}")
    @Operation(
            summary = "Get sales report for seller",
            description = "Calculate total sales for a seller within a date range"
    )
    @PreAuthorize("hasRole('ADMIN') or hasRole('BOSS')")
    public ResponseEntity<Map<String, Object>> getSalesReport(
            @Parameter(description = "Seller user ID", required = true)
            @PathVariable Long sellerId,
            @Parameter(description = "Start date (yyyy-MM-dd'T'HH:mm:ss)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date (yyyy-MM-dd'T'HH:mm:ss)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        BigDecimal totalSales = transactionService.getTotalSalesBySellerAndDateRange(sellerId, startDate, endDate);

        Map<String, Object> report = Map.of(
                "sellerId", sellerId,
                "startDate", startDate,
                "endDate", endDate,
                "totalSales", totalSales != null ? totalSales : BigDecimal.ZERO,
                "currency", "USD"
        );

        return ResponseEntity.ok(report);
    }
}