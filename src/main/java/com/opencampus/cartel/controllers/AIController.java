package com.opencampus.cartel.controllers;

import com.opencampus.cartel.services.AIService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@Tag(name = "AI Services", description = "AI-powered features using Mistral AI")
@SecurityRequirement(name = "bearerAuth")
public class AIController {

    @Autowired
    private AIService aiService;

    @GetMapping("/operation-suggestions")
    @Operation(
            summary = "Get operation suggestions",
            description = "Get AI-generated suggestions for new operations based on current context"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Suggestions generated successfully",
                    content = @Content(schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(responseCode = "500", description = "AI service error")
    })
    public ResponseEntity<?> getOperationSuggestions(
            @Parameter(description = "Territory for suggestions", required = false)
            @RequestParam(required = false) String territory,
            @Parameter(description = "Risk level preference (1-10)", required = false)
            @RequestParam(required = false) Integer riskLevel) {
        try {
            Map<String, Object> suggestions = aiService.generateOperationSuggestions(territory, riskLevel);
            return ResponseEntity.ok(suggestions);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "AI service unavailable: " + e.getMessage()));
        }
    }

    @GetMapping("/product-analysis")
    @Operation(
            summary = "Get product market analysis",
            description = "Get AI analysis of product market trends and pricing recommendations"
    )
    public ResponseEntity<?> getProductAnalysis(
            @Parameter(description = "Product ID for analysis", required = true)
            @RequestParam Long productId) {
        try {
            Map<String, Object> analysis = aiService.analyzeProductMarket(productId);
            return ResponseEntity.ok(analysis);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "AI service unavailable: " + e.getMessage()));
        }
    }

    @GetMapping("/risk-assessment")
    @Operation(
            summary = "Get operation risk assessment",
            description = "Get AI-powered risk assessment for an operation"
    )
    public ResponseEntity<?> getRiskAssessment(
            @Parameter(description = "Operation ID for risk assessment", required = true)
            @RequestParam Long operationId) {
        try {
            Map<String, Object> riskAssessment = aiService.assessOperationRisk(operationId);
            return ResponseEntity.ok(riskAssessment);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "AI service unavailable: " + e.getMessage()));
        }
    }

    @PostMapping("/chat")
    @Operation(
            summary = "Chat with AI assistant",
            description = "Interactive chat with AI for strategic advice and planning"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "AI response generated successfully",
                    content = @Content(schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid message"),
            @ApiResponse(responseCode = "500", description = "AI service error")
    })
    public ResponseEntity<?> chatWithAI(
            @Parameter(description = "Message to send to AI", required = true)
            @RequestBody Map<String, String> request) {
        try {
            String message = request.get("message");
            if (message == null || message.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Message is required"));
            }

            String response = aiService.chatWithAI(message);
            return ResponseEntity.ok(Map.of(
                    "message", message,
                    "response", response,
                    "timestamp", System.currentTimeMillis()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "AI service unavailable: " + e.getMessage()));
        }
    }

    @GetMapping("/financial-forecast")
    @Operation(
            summary = "Get financial forecast",
            description = "Get AI-generated financial forecasts based on current operations"
    )
    public ResponseEntity<?> getFinancialForecast(
            @Parameter(description = "Number of months to forecast", required = false)
            @RequestParam(defaultValue = "3") Integer months) {
        try {
            Map<String, Object> forecast = aiService.generateFinancialForecast(months);
            return ResponseEntity.ok(forecast);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "AI service unavailable: " + e.getMessage()));
        }
    }
}