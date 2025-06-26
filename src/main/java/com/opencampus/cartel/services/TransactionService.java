package com.opencampus.cartel.services;


import com.opencampus.cartel.dto.request.TransactionCreateRequest;
import com.opencampus.cartel.dto.response.TransactionResponse;
import com.opencampus.cartel.model.entity.Operation;
import com.opencampus.cartel.model.entity.Product;
import com.opencampus.cartel.model.entity.Transaction;
import com.opencampus.cartel.model.entity.User;
import com.opencampus.cartel.repository.OperationRepository;
import com.opencampus.cartel.repository.ProductRepository;
import com.opencampus.cartel.repository.TransactionRepository;
import com.opencampus.cartel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OperationService operationService;

    public TransactionResponse createTransaction(TransactionCreateRequest request) {
        Operation operation = operationRepository.findById(request.getOperationId())
                .orElseThrow(() -> new RuntimeException("Operation not found"));
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        User buyer = userRepository.findById(request.getBuyerId())
                .orElseThrow(() -> new RuntimeException("Buyer not found"));
        User seller = userRepository.findById(request.getSellerId())
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        Transaction transaction = new Transaction();
        transaction.setOperation(operation);
        transaction.setProduct(product);
        transaction.setBuyer(buyer);
        transaction.setSeller(seller);
        transaction.setQuantity(request.getQuantity());
        transaction.setUnitPrice(request.getUnitPrice());
        transaction.setTotalAmount(request.getUnitPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
        transaction.setTransactionLocation(request.getTransactionLocation());
        transaction.setNotes(request.getNotes());

        Transaction savedTransaction = transactionRepository.save(transaction);
        return convertToResponse(savedTransaction);
    }

    public TransactionResponse getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        return convertToResponse(transaction);
    }

    public Page<TransactionResponse> getAllTransactions(Pageable pageable) {
        return transactionRepository.findAll(pageable).map(this::convertToResponse);
    }

    public Page<TransactionResponse> getTransactionsByUser(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return transactionRepository.findTransactionsByUser(user, pageable).map(this::convertToResponse);
    }

    public List<TransactionResponse> getTransactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findTransactionsByDateRange(startDate, endDate)
                .stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    public BigDecimal getTotalSalesBySellerAndDateRange(Long sellerId, LocalDateTime startDate, LocalDateTime endDate) {
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new RuntimeException("Seller not found"));
        return transactionRepository.calculateTotalSalesBySellerAndDateRange(seller, startDate, endDate);
    }

    private TransactionResponse convertToResponse(Transaction transaction) {
        TransactionResponse response = new TransactionResponse();
        response.setId(transaction.getId());
        response.setOperation(operationService.convertToResponse(transaction.getOperation()));
        response.setProduct(productService.convertToResponse(transaction.getProduct()));
        response.setBuyer(userService.convertToResponse(transaction.getBuyer()));
        response.setSeller(userService.convertToResponse(transaction.getSeller()));
        response.setQuantity(transaction.getQuantity());
        response.setUnitPrice(transaction.getUnitPrice());
        response.setTotalAmount(transaction.getTotalAmount());
        response.setTransactionLocation(transaction.getTransactionLocation());
        response.setNotes(transaction.getNotes());
        response.setTransactionDate(transaction.getTransactionDate());
        return response;
    }
}