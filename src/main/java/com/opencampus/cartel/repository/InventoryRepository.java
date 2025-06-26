package com.opencampus.cartel.repository;

import com.opencampus.cartel.model.entity.Inventory;
import com.opencampus.cartel.model.entity.Product;
import com.opencampus.cartel.model.entity.Warehouse;
import com.opencampus.cartel.model.enums.ProductType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    List<Inventory> findByWarehouse(Warehouse warehouse);

    List<Inventory> findByProduct(Product product);

    Optional<Inventory> findByProductAndWarehouse(Product product, Warehouse warehouse);

    Page<Inventory> findByWarehouse(Warehouse warehouse, Pageable pageable);

    @Query("SELECT i FROM Inventory i WHERE i.quantity > 0")
    List<Inventory> findAvailableInventory();

    @Query("SELECT i FROM Inventory i WHERE i.quantity <= i.minimumStockLevel")
    List<Inventory> findLowStockInventory();

    @Query("SELECT i FROM Inventory i WHERE i.warehouse = :warehouse AND i.quantity > 0")
    List<Inventory> findAvailableInventoryByWarehouse(@Param("warehouse") Warehouse warehouse);

    @Query("SELECT SUM(i.quantity) FROM Inventory i WHERE i.product = :product")
    Integer getTotalQuantityByProduct(@Param("product") Product product);

    @Query("SELECT i FROM Inventory i WHERE i.product.productType = :productType AND i.quantity > 0")
    List<Inventory> findAvailableInventoryByProductType(@Param("productType") ProductType productType);

    @Query("SELECT i FROM Inventory i WHERE i.warehouse.city = :city AND i.quantity > 0")
    List<Inventory> findAvailableInventoryByCity(@Param("city") String city);
}