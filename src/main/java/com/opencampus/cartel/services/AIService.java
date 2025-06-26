package com.opencampus.cartel.services;

import com.opencampus.cartel.model.entity.Operation;
import com.opencampus.cartel.model.entity.Product;
import com.opencampus.cartel.repository.OperationRepository;
import com.opencampus.cartel.repository.ProductRepository;
import com.opencampus.cartel.repository.TransactionRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AIService {

    private final ChatClient chatClient;

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public AIService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public Map<String, Object> generateOperationSuggestions(String territory, Integer riskLevel) {
        try {
            // Get context from existing operations
            List<Operation> recentOperations = operationRepository.findOperationsByDateRange(
                    LocalDateTime.now().minusMonths(3), LocalDateTime.now()
            );

            StringBuilder prompt = new StringBuilder();
            prompt.append("As a strategic advisor for a fictional cartel business simulation, ");
            prompt.append("suggest 3 new operation ideas based on the following context:\n");
            prompt.append("Territory: ").append(territory != null ? territory : "Any").append("\n");
            prompt.append("Preferred risk level: ").append(riskLevel != null ? riskLevel : "Medium").append(" (1-10 scale)\n");
            prompt.append("Recent operations count: ").append(recentOperations.size()).append("\n");
            prompt.append("Provide creative but realistic operation names, descriptions, and estimated risk levels.");

            String aiResponse = chatClient.prompt()
                    .user(prompt.toString())
                    .call()
                    .content();

            Map<String, Object> result = new HashMap<>();
            result.put("suggestions", aiResponse);
            result.put("territory", territory);
            result.put("requestedRiskLevel", riskLevel);
            result.put("contextOperations", recentOperations.size());
            result.put("generatedAt", LocalDateTime.now());

            return result;
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate operation suggestions: " + e.getMessage());
        }
    }

    public Map<String, Object> analyzeProductMarket(Long productId) {
        try {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            String prompt = String.format(
                    "Analyze the market for this fictional product in a business simulation:\n" +
                            "Product: %s (%s)\n" +
                            "Type: %s\n" +
                            "Current price: $%s per %s\n" +
                            "Origin: %s\n" +
                            "Purity: %d%%\n\n" +
                            "Provide market analysis including:\n" +
                            "1. Pricing recommendations\n" +
                            "2. Market trends\n" +
                            "3. Competitive positioning\n" +
                            "4. Risk factors",
                    product.getName(),
                    product.getCodeName(),
                    product.getProductType(),
                    product.getPricePerUnit(),
                    product.getUnitMeasure(),
                    product.getOriginCountry(),
                    product.getPurityLevel()
            );

            String aiResponse = chatClient.prompt()
                    .user(prompt)
                    .call()
                    .content();

            Map<String, Object> result = new HashMap<>();
            result.put("productId", productId);
            result.put("productName", product.getName());
            result.put("analysis", aiResponse);
            result.put("currentPrice", product.getPricePerUnit());
            result.put("analyzedAt", LocalDateTime.now());

            return result;
        } catch (Exception e) {
            throw new RuntimeException("Failed to analyze product market: " + e.getMessage());
        }
    }

    public Map<String, Object> assessOperationRisk(Long operationId) {
        try {
            Operation operation = operationRepository.findById(operationId)
                    .orElseThrow(() -> new RuntimeException("Operation not found"));

            String prompt = String.format(
                    "Assess the risk for this fictional business operation:\n" +
                            "Operation: %s (%s)\n" +
                            "Location: %s\n" +
                            "Current risk level: %d/10\n" +
                            "Participants: %d\n" +
                            "Status: %s\n" +
                            "Estimated profit: $%s\n\n" +
                            "Provide a detailed risk assessment including:\n" +
                            "1. Risk factors analysis\n" +
                            "2. Mitigation strategies\n" +
                            "3. Success probability\n" +
                            "4. Contingency recommendations",
                    operation.getName(),
                    operation.getCodeName(),
                    operation.getLocation(),
                    operation.getRiskLevel(),
                    operation.getParticipants() != null ? operation.getParticipants().size() : 0,
                    operation.getStatus(),
                    operation.getEstimatedProfit()
            );

            String aiResponse = chatClient.prompt()
                    .user(prompt)
                    .call()
                    .content();

            Map<String, Object> result = new HashMap<>();
            result.put("operationId", operationId);
            result.put("operationName", operation.getName());
            result.put("currentRiskLevel", operation.getRiskLevel());
            result.put("riskAssessment", aiResponse);
            result.put("assessedAt", LocalDateTime.now());

            return result;
        } catch (Exception e) {
            throw new RuntimeException("Failed to assess operation risk: " + e.getMessage());
        }
    }

    public String chatWithAI(String message) {
        try {
            String contextualPrompt = "You are a strategic business advisor for a fictional cartel simulation game. " +
                    "Provide helpful, creative advice while keeping responses appropriate and clearly fictional. " +
                    "User question: " + message;

            return chatClient.prompt()
                    .user(contextualPrompt)
                    .call()
                    .content();
        } catch (Exception e) {
            throw new RuntimeException("Failed to process chat message: " + e.getMessage());
        }
    }

    public Map<String, Object> generateFinancialForecast(Integer months) {
        try {
            // Get historical transaction data
            LocalDateTime threeMonthsAgo = LocalDateTime.now().minusMonths(3);
            List<Object[]> historicalData = transactionRepository.findTransactionsByDateRange(threeMonthsAgo, LocalDateTime.now())
                    .stream()
                    .map(t -> new Object[]{t.getTransactionDate(), t.getTotalAmount()})
                    .toList();

            String prompt = String.format(
                    "Generate a financial forecast for the next %d months based on this fictional business data:\n" +
                            "Historical transactions: %d transactions in last 3 months\n" +
                            "Provide forecasting including:\n" +
                            "1. Revenue projections\n" +
                            "2. Market trends analysis\n" +
                            "3. Growth opportunities\n" +
                            "4. Risk factors\n" +
                            "5. Strategic recommendations",
                    months,
                    historicalData.size()
            );

            String aiResponse = chatClient.prompt()
                    .user(prompt)
                    .call()
                    .content();

            Map<String, Object> result = new HashMap<>();
            result.put("forecastPeriod", months + " months");
            result.put("historicalDataPoints", historicalData.size());
            result.put("forecast", aiResponse);
            result.put("generatedAt", LocalDateTime.now());

            return result;
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate financial forecast: " + e.getMessage());
        }
    }
}