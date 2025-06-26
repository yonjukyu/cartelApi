package com.opencampus.cartel.dto.response;

import com.opencampus.cartel.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private String codeName;
    private Role role;

    public AuthResponse(String token, Long id, String username, String email, String codeName, Role role) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
        this.codeName = codeName;
        this.role = role;
    }
}