package com.opencampus.cartel.repository;

import com.opencampus.cartel.model.entity.Product;
import com.opencampus.cartel.model.entity.Transaction;
import com.opencampus.cartel.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByBuyer(User buyer);

    List<Transaction> findBySeller(User seller);

    Page<Transaction> findByBuyer(User buyer, Pageable pageable);

    Page<Transaction> findBySeller(User seller, Pageable pageable);

    List<Transaction> findByProduct(Product product);

    @Query("SELECT t FROM Transaction t WHERE t.buyer = :user OR t.seller = :user")
    Page<Transaction> findTransactionsByUser(@Param("user") User user, Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE t.transactionDate BETWEEN :startDate AND :endDate")
    List<Transaction> findTransactionsByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT SUM(t.totalAmount) FROM Transaction t WHERE t.seller = :seller AND t.transactionDate BETWEEN :startDate AND :endDate")
    BigDecimal calculateTotalSalesBySellerAndDateRange(@Param("seller") User seller, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT SUM(t.totalAmount) FROM Transaction t WHERE t.buyer = :buyer AND t.transactionDate BETWEEN :startDate AND :endDate")
    BigDecimal calculateTotalPurchasesByBuyerAndDateRange(@Param("buyer") User buyer, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT t FROM Transaction t WHERE t.transactionLocation = :location")
    List<Transaction> findByTransactionLocation(@Param("location") String location);

    @Query("SELECT t FROM Transaction t WHERE t.totalAmount >= :minAmount ORDER BY t.totalAmount DESC")
    List<Transaction> findHighValueTransactions(@Param("minAmount") BigDecimal minAmount);
}