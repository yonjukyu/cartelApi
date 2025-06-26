package com.opencampus.cartel.repository;

import com.opencampus.cartel.model.entity.User;
import com.opencampus.cartel.model.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<User> findByRole(Role role);

    Page<User> findByRoleAndIsActiveTrue(Role role, Pageable pageable);

    Page<User> findByIsActiveTrue(Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.territory = :territory AND u.isActive = true")
    List<User> findActiveUsersByTerritory(@Param("territory") String territory);

    @Query("SELECT u FROM User u WHERE u.codeName LIKE %:codeName% AND u.isActive = true")
    Page<User> findByCodeNameContainingAndIsActiveTrue(@Param("codeName") String codeName, Pageable pageable);
}