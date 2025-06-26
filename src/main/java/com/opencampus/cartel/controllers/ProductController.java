package com.opencampus.cartel.controllers;

import com.opencampus.cartel.dto.request.ProductCreateRequest;
import com.opencampus.cartel.dto.request.ProductUpdateRequest;
import com.opencampus.cartel.dto.response.ProductResponse;
import com.opencampus.cartel.model.enums.ProductType;
import com.opencampus.cartel.services.ProductService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Products", description = "Product management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    @Operation(
            summary = "Create a new product",
            description = "Add a new product to the catalog. Admin and Boss only."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Product created successfully",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input or product code already exists"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('BOSS')")
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductCreateRequest request) {
        try {
            ProductResponse response = productService.createProduct(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get product by ID",
            description = "Retrieve product information by ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Product found",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<?> getProductById(
            @Parameter(description = "Product ID", required = true)
            @PathVariable Long id) {
        try {
            ProductResponse response = productService.getProductById(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/code/{codeName}")
    @Operation(
            summary = "Get product by code name",
            description = "Retrieve product information by code name"
    )
    public ResponseEntity<?> getProductByCodeName(
            @Parameter(description = "Product code name", required = true)
            @PathVariable String codeName) {
        try {
            ProductResponse response = productService.getProductByCodeName(codeName);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    @Operation(
            summary = "Get all products",
            description = "Retrieve all available products with pagination"
    )
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field", example = "name")
            @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction", example = "asc")
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ProductResponse> products = productService.getAllProducts(pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/type/{productType}")
    @Operation(
            summary = "Get products by type",
            description = "Retrieve products filtered by product type"
    )
    public ResponseEntity<Page<ProductResponse>> getProductsByType(
            @Parameter(description = "Product type", required = true)
            @PathVariable ProductType productType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ProductResponse> products = productService.getProductsByType(productType, pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/search")
    @Operation(
            summary = "Search products by name",
            description = "Search products by name with pagination"
    )
    public ResponseEntity<Page<ProductResponse>> searchProductsByName(
            @Parameter(description = "Product name to search", required = true)
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ProductResponse> products = productService.searchProductsByName(name, pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/price-range")
    @Operation(
            summary = "Get products by price range",
            description = "Retrieve products within a specific price range"
    )
    public ResponseEntity<List<ProductResponse>> getProductsByPriceRange(
            @Parameter(description = "Minimum price", required = true)
            @RequestParam BigDecimal minPrice,
            @Parameter(description = "Maximum price", required = true)
            @RequestParam BigDecimal maxPrice) {

        List<ProductResponse> products = productService.getProductsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/origin/{country}")
    @Operation(
            summary = "Get products by origin country",
            description = "Retrieve products from a specific country"
    )
    public ResponseEntity<List<ProductResponse>> getProductsByOriginCountry(
            @Parameter(description = "Origin country", required = true)
            @PathVariable String country) {

        List<ProductResponse> products = productService.getProductsByOriginCountry(country);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/purity/{minPurity}")
    @Operation(
            summary = "Get products by minimum purity",
            description = "Retrieve products with minimum purity level"
    )
    public ResponseEntity<List<ProductResponse>> getProductsByMinimumPurity(
            @Parameter(description = "Minimum purity level (0-100)", required = true)
            @PathVariable Integer minPurity) {

        List<ProductResponse> products = productService.getProductsByMinimumPurity(minPurity);
        return ResponseEntity.ok(products);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update product",
            description = "Update product information. Admin and Boss only."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Product updated successfully",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('BOSS')")
    public ResponseEntity<?> updateProduct(
            @Parameter(description = "Product ID", required = true)
            @PathVariable Long id,
            @Valid @RequestBody ProductUpdateRequest request) {
        try {
            ProductResponse response = productService.updateProduct(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Discontinue product",
            description = "Mark product as unavailable (soft delete). Admin and Boss only."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product discontinued successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('BOSS')")
    public ResponseEntity<?> deleteProduct(
            @Parameter(description = "Product ID", required = true)
            @PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok(Map.of("message", "Product discontinued successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}