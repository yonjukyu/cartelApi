package com.opencampus.cartel.repository;

import com.opencampus.cartel.model.entity.Product;
import com.opencampus.cartel.model.enums.ProductType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByCodeName(String codeName);

    boolean existsByCodeName(String codeName);

    List<Product> findByProductType(ProductType productType);

    Page<Product> findByProductTypeAndIsAvailableTrue(ProductType productType, Pageable pageable);

    Page<Product> findByIsAvailableTrue(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.name LIKE %:name% AND p.isAvailable = true")
    Page<Product> findByNameContainingAndIsAvailableTrue(@Param("name") String name, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.pricePerUnit BETWEEN :minPrice AND :maxPrice AND p.isAvailable = true")
    List<Product> findByPriceRange(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);

    @Query("SELECT p FROM Product p WHERE p.originCountry = :country AND p.isAvailable = true")
    List<Product> findByOriginCountry(@Param("country") String country);

    @Query("SELECT p FROM Product p WHERE p.purityLevel >= :minPurity AND p.isAvailable = true")
    List<Product> findByMinimumPurity(@Param("minPurity") Integer minPurity);
}