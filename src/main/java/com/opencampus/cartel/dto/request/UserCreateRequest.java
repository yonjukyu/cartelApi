package com.opencampus.cartel.dto.request;


import com.opencampus.cartel.model.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

// UserCreateRequest.java
@Data
public class UserCreateRequest {
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Code name is required")
    @Size(min = 2, max = 50, message = "Code name must be between 2 and 50 characters")
    private String codeName;

    private Role role = Role.USER;
    private String phoneNumber;
    private String territory;
}