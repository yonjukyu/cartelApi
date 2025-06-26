package com.opencampus.cartel.model.entity;

import com.opencampus.cartel.model.enums.ProductType;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Code name is required")
    @Size(min = 2, max = 50, message = "Code name must be between 2 and 50 characters")
    @Column(name = "code_name", unique = true, nullable = false)
    private String codeName;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Product type is required")
    @Column(name = "product_type", nullable = false)
    private ProductType productType;

    @Column(length = 500)
    private String description;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price per unit must be greater than 0")
    @Column(name = "price_per_unit", precision = 10, scale = 2)
    private BigDecimal pricePerUnit;

    @Column(name = "unit_measure")
    private String unitMeasure; // kg, g, pills, liters, etc.

    @Column(name = "origin_country")
    private String originCountry;

    @Column(name = "purity_level")
    private Integer purityLevel; // 0-100%

    @Column(name = "is_available")
    private Boolean isAvailable = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relationships
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Inventory> inventories;
}