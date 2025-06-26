package com.opencampus.cartel.services;

import com.opencampus.cartel.dto.request.UserCreateRequest;
import com.opencampus.cartel.dto.request.UserUpdateRequest;
import com.opencampus.cartel.dto.response.UserResponse;
import com.opencampus.cartel.model.entity.User;
import com.opencampus.cartel.model.enums.Role;
import com.opencampus.cartel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserCreateRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setCodeName(request.getCodeName());
        user.setRole(request.getRole());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setTerritory(request.getTerritory());
        user.setIsActive(true);

        User savedUser = userRepository.save(user);
        return convertToResponse(savedUser);
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return convertToResponse(user);
    }

    public Page<UserResponse> getAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findByIsActiveTrue(pageable);
        return users.map(this::convertToResponse);
    }

    public Page<UserResponse> getUsersByRole(Role role, Pageable pageable) {
        Page<User> users = userRepository.findByRoleAndIsActiveTrue(role, pageable);
        return users.map(this::convertToResponse);
    }

    public List<UserResponse> getUsersByTerritory(String territory) {
        List<User> users = userRepository.findActiveUsersByTerritory(territory);
        return users.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public Page<UserResponse> searchUsersByCodeName(String codeName, Pageable pageable) {
        Page<User> users = userRepository.findByCodeNameContainingAndIsActiveTrue(codeName, pageable);
        return users.map(this::convertToResponse);
    }

    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new RuntimeException("Email already exists");
            }
            user.setEmail(request.getEmail());
        }

        if (request.getCodeName() != null) {
            user.setCodeName(request.getCodeName());
        }
        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }
        if (request.getPhoneNumber() != null) {
            user.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getTerritory() != null) {
            user.setTerritory(request.getTerritory());
        }
        if (request.getIsActive() != null) {
            user.setIsActive(request.getIsActive());
        }

        User updatedUser = userRepository.save(user);
        return convertToResponse(updatedUser);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.setIsActive(false);
        userRepository.save(user);
    }

    UserResponse convertToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setCodeName(user.getCodeName());
        response.setRole(user.getRole());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setTerritory(user.getTerritory());
        response.setIsActive(user.getIsActive());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }
}