package com.opencampus.cartel.repository;
import com.opencampus.cartel.model.entity.Operation;
import com.opencampus.cartel.model.entity.User;
import com.opencampus.cartel.model.enums.OperationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {

    List<Operation> findByLeader(User leader);

    Page<Operation> findByLeader(User leader, Pageable pageable);

    List<Operation> findByStatus(OperationStatus status);

    Page<Operation> findByStatus(OperationStatus status, Pageable pageable);

    @Query("SELECT o FROM Operation o WHERE o.leader = :leader AND o.status = :status")
    List<Operation> findByLeaderAndStatus(@Param("leader") User leader, @Param("status") OperationStatus status);

    @Query("SELECT o FROM Operation o WHERE :user MEMBER OF o.participants")
    List<Operation> findOperationsByParticipant(@Param("user") User user);

    @Query("SELECT o FROM Operation o WHERE o.startDate BETWEEN :startDate AND :endDate")
    List<Operation> findOperationsByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT o FROM Operation o WHERE o.location = :location AND o.status != 'CANCELLED'")
    List<Operation> findActiveOperationsByLocation(@Param("location") String location);

    @Query("SELECT o FROM Operation o WHERE o.riskLevel >= :minRisk ORDER BY o.riskLevel DESC")
    List<Operation> findHighRiskOperations(@Param("minRisk") Integer minRisk);

    @Query("SELECT o FROM Operation o WHERE o.name LIKE %:name% OR o.codeName LIKE %:codeName%")
    Page<Operation> findByNameOrCodeNameContaining(@Param("name") String name, @Param("codeName") String codeName, Pageable pageable);
}