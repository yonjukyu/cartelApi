package com.opencampus.cartel.dto.request;

import com.opencampus.cartel.model.enums.ProductType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductUpdateRequest {
    @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
    private String name;

    private ProductType productType;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price per unit must be greater than 0")
    private BigDecimal pricePerUnit;

    private String unitMeasure;
    private String originCountry;
    private Integer purityLevel;
    private Boolean isAvailable;
}