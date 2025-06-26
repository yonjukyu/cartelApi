package com.opencampus.cartel.services;

import com.opencampus.cartel.dto.request.OperationCreateRequest;
import com.opencampus.cartel.dto.request.OperationUpdateRequest;
import com.opencampus.cartel.dto.response.OperationResponse;
import com.opencampus.cartel.dto.response.UserResponse;
import com.opencampus.cartel.model.entity.Operation;
import com.opencampus.cartel.model.entity.User;
import com.opencampus.cartel.model.enums.OperationStatus;
import com.opencampus.cartel.repository.OperationRepository;
import com.opencampus.cartel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OperationService {

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    public OperationResponse createOperation(OperationCreateRequest request) {
        User leader = userRepository.findById(request.getLeaderId())
                .orElseThrow(() -> new RuntimeException("Leader not found with id: " + request.getLeaderId()));

        Operation operation = new Operation();
        operation.setName(request.getName());
        operation.setCodeName(request.getCodeName());
        operation.setDescription(request.getDescription());
        operation.setLeader(leader);
        operation.setStatus(OperationStatus.PLANNED);
        operation.setStartDate(request.getStartDate());
        operation.setEndDate(request.getEndDate());
        operation.setLocation(request.getLocation());
        operation.setRiskLevel(request.getRiskLevel());
        operation.setEstimatedProfit(request.getEstimatedProfit());

        // Add participants
        if (request.getParticipantIds() != null && !request.getParticipantIds().isEmpty()) {
            List<User> participants = userRepository.findAllById(request.getParticipantIds());
            operation.setParticipants(participants);
        }

        Operation savedOperation = operationRepository.save(operation);
        return convertToResponse(savedOperation);
    }

    public OperationResponse getOperationById(Long id) {
        Operation operation = operationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Operation not found with id: " + id));
        return convertToResponse(operation);
    }

    public Page<OperationResponse> getAllOperations(Pageable pageable) {
        Page<Operation> operations = operationRepository.findAll(pageable);
        return operations.map(this::convertToResponse);
    }

    public Page<OperationResponse> getOperationsByLeader(Long leaderId, Pageable pageable) {
        User leader = userRepository.findById(leaderId)
                .orElseThrow(() -> new RuntimeException("Leader not found with id: " + leaderId));
        Page<Operation> operations = operationRepository.findByLeader(leader, pageable);
        return operations.map(this::convertToResponse);
    }

    public Page<OperationResponse> getOperationsByStatus(OperationStatus status, Pageable pageable) {
        Page<Operation> operations = operationRepository.findByStatus(status, pageable);
        return operations.map(this::convertToResponse);
    }

    public List<OperationResponse> getOperationsByParticipant(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        List<Operation> operations = operationRepository.findOperationsByParticipant(user);
        return operations.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<OperationResponse> getOperationsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<Operation> operations = operationRepository.findOperationsByDateRange(startDate, endDate);
        return operations.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<OperationResponse> getActiveOperationsByLocation(String location) {
        List<Operation> operations = operationRepository.findActiveOperationsByLocation(location);
        return operations.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<OperationResponse> getHighRiskOperations(Integer minRisk) {
        List<Operation> operations = operationRepository.findHighRiskOperations(minRisk);
        return operations.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public Page<OperationResponse> searchOperations(String searchTerm, Pageable pageable) {
        Page<Operation> operations = operationRepository.findByNameOrCodeNameContaining(searchTerm, searchTerm, pageable);
        return operations.map(this::convertToResponse);
    }

    public OperationResponse updateOperation(Long id, OperationUpdateRequest request) {
        Operation operation = operationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Operation not found with id: " + id));

        if (request.getName() != null) {
            operation.setName(request.getName());
        }
        if (request.getDescription() != null) {
            operation.setDescription(request.getDescription());
        }
        if (request.getStatus() != null) {
            operation.setStatus(request.getStatus());
        }
        if (request.getStartDate() != null) {
            operation.setStartDate(request.getStartDate());
        }
        if (request.getEndDate() != null) {
            operation.setEndDate(request.getEndDate());
        }
        if (request.getLocation() != null) {
            operation.setLocation(request.getLocation());
        }
        if (request.getRiskLevel() != null) {
            operation.setRiskLevel(request.getRiskLevel());
        }
        if (request.getEstimatedProfit() != null) {
            operation.setEstimatedProfit(request.getEstimatedProfit());
        }
        if (request.getActualProfit() != null) {
            operation.setActualProfit(request.getActualProfit());
        }

        // Update participants
        if (request.getParticipantIds() != null) {
            List<User> participants = userRepository.findAllById(request.getParticipantIds());
            operation.setParticipants(participants);
        }

        Operation updatedOperation = operationRepository.save(operation);
        return convertToResponse(updatedOperation);
    }

    public void deleteOperation(Long id) {
        Operation operation = operationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Operation not found with id: " + id));
        operation.setStatus(OperationStatus.CANCELLED);
        operationRepository.save(operation);
    }

    OperationResponse convertToResponse(Operation operation) {
        OperationResponse response = new OperationResponse();
        response.setId(operation.getId());
        response.setName(operation.getName());
        response.setCodeName(operation.getCodeName());
        response.setDescription(operation.getDescription());
        response.setLeader(userService.convertToResponse(operation.getLeader()));
        response.setStatus(operation.getStatus());
        response.setStartDate(operation.getStartDate());
        response.setEndDate(operation.getEndDate());
        response.setLocation(operation.getLocation());
        response.setRiskLevel(operation.getRiskLevel());
        response.setEstimatedProfit(operation.getEstimatedProfit());
        response.setActualProfit(operation.getActualProfit());
        response.setCreatedAt(operation.getCreatedAt());
        response.setUpdatedAt(operation.getUpdatedAt());

        // Convert participants
        if (operation.getParticipants() != null) {
            List<UserResponse> participants = operation.getParticipants().stream()
                    .map(userService::convertToResponse)
                    .collect(Collectors.toList());
            response.setParticipants(participants);
        }

        return response;
    }
}