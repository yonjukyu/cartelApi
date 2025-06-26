package com.opencampus.cartel.dto.request;

import com.opencampus.cartel.model.enums.Role;
import jakarta.validation.constraints.Email;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class UserUpdateRequest {
    @Email(message = "Email should be valid")
    private String email;

    @Size(min = 2, max = 50, message = "Code name must be between 2 and 50 characters")
    private String codeName;

    private Role role;
    private String phoneNumber;
    private String territory;
    private Boolean isActive;
}