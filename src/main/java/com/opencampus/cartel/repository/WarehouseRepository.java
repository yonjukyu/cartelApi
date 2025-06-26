package com.opencampus.cartel.repository;

import com.opencampus.cartel.model.entity.User;
import com.opencampus.cartel.model.entity.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    Optional<Warehouse> findByCodeName(String codeName);

    boolean existsByCodeName(String codeName);

    List<Warehouse> findByManager(User manager);

    Page<Warehouse> findByIsActiveTrue(Pageable pageable);

    @Query("SELECT w FROM Warehouse w WHERE w.city = :city AND w.isActive = true")
    List<Warehouse> findActiveWarehousesByCity(@Param("city") String city);

    @Query("SELECT w FROM Warehouse w WHERE w.country = :country AND w.isActive = true")
    List<Warehouse> findActiveWarehousesByCountry(@Param("country") String country);

    @Query("SELECT w FROM Warehouse w WHERE w.securityLevel >= :minLevel AND w.isActive = true")
    List<Warehouse> findByMinimumSecurityLevel(@Param("minLevel") Integer minLevel);

    @Query("SELECT w FROM Warehouse w WHERE w.name LIKE %:name% AND w.isActive = true")
    Page<Warehouse> findByNameContainingAndIsActiveTrue(@Param("name") String name, Pageable pageable);

    @Query("SELECT w FROM Warehouse w WHERE w.capacity >= :minCapacity AND w.isActive = true")
    List<Warehouse> findByMinimumCapacity(@Param("minCapacity") Integer minCapacity);
}