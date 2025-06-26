package com.opencampus.cartel.controllers;

import com.opencampus.cartel.dto.request.MessageCreateRequest;
import com.opencampus.cartel.dto.response.MessageResponse;
import com.opencampus.cartel.services.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
@Tag(name = "Messages", description = "Internal messaging system endpoints")
@SecurityRequirement(name = "bearerAuth")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping
    @Operation(
            summary = "Send a message",
            description = "Send a secure message to another cartel member"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Message sent successfully",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Receiver not found")
    })
    public ResponseEntity<?> sendMessage(@Valid @RequestBody MessageCreateRequest request) {
        try {
            MessageResponse response = messageService.sendMessage(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get message by ID",
            description = "Retrieve a specific message"
    )
    public ResponseEntity<?> getMessageById(
            @Parameter(description = "Message ID", required = true)
            @PathVariable Long id) {
        try {
            MessageResponse response = messageService.getMessageById(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/inbox")
    @Operation(
            summary = "Get received messages",
            description = "Get messages received by the current user"
    )
    public ResponseEntity<Page<MessageResponse>> getReceivedMessages(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("sentAt").descending());
        Page<MessageResponse> messages = messageService.getReceivedMessages(pageable);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/sent")
    @Operation(
            summary = "Get sent messages",
            description = "Get messages sent by the current user"
    )
    public ResponseEntity<Page<MessageResponse>> getSentMessages(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("sentAt").descending());
        Page<MessageResponse> messages = messageService.getSentMessages(pageable);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/unread")
    @Operation(
            summary = "Get unread messages",
            description = "Get all unread messages for the current user"
    )
    public ResponseEntity<List<MessageResponse>> getUnreadMessages() {
        List<MessageResponse> messages = messageService.getUnreadMessages();
        return ResponseEntity.ok(messages);
    }

    @PutMapping("/{id}/read")
    @Operation(
            summary = "Mark message as read",
            description = "Mark a specific message as read"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Message marked as read",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Message not found"),
            @ApiResponse(responseCode = "403", description = "Not authorized to mark this message as read")
    })
    public ResponseEntity<?> markAsRead(
            @Parameter(description = "Message ID", required = true)
            @PathVariable Long id) {
        try {
            MessageResponse response = messageService.markAsRead(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}