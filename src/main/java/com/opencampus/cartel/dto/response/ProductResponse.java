package com.opencampus.cartel.dto.response;

import com.opencampus.cartel.model.enums.ProductType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductResponse {
    private Long id;
    private String name;
    private String codeName;
    private ProductType productType;
    private String description;
    private BigDecimal pricePerUnit;
    private String unitMeasure;
    private String originCountry;
    private Integer purityLevel;
    private Boolean isAvailable;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}