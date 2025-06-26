package com.opencampus.cartel.model.entity;
import com.opencampus.cartel.model.enums.OperationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "operations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Operation name is required")
    @Size(min = 3, max = 100, message = "Operation name must be between 3 and 100 characters")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Code name is required")
    @Size(min = 3, max = 50, message = "Code name must be between 3 and 50 characters")
    @Column(name = "code_name", nullable = false)
    private String codeName;

    @Column(length = 1000)
    private String description;

    @NotNull(message = "Leader is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leader_id", nullable = false)
    private User leader;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OperationStatus status = OperationStatus.PLANNED;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "location")
    private String location;

    @Column(name = "risk_level")
    private Integer riskLevel; // 1-10

    @Column(name = "estimated_profit", precision = 12, scale = 2)
    private java.math.BigDecimal estimatedProfit;

    @Column(name = "actual_profit", precision = 12, scale = 2)
    private java.math.BigDecimal actualProfit;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relationships
    @ManyToMany
    @JoinTable(
            name = "operation_participants",
            joinColumns = @JoinColumn(name = "operation_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> participants;

    @OneToMany(mappedBy = "operation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions;
}