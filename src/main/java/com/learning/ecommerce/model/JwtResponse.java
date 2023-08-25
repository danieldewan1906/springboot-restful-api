package com.learning.ecommerce.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class JwtResponse implements Serializable{
    
    private String token;
    private String refreshToken;
    private String type = "Bearer";
    private String username;
    private String email;
    private String role;

    public JwtResponse(String accessToken, String refreshToken, String username, String email, String role) {
        this.username = username;
        this.token = accessToken;
        this.email = email;
        this.refreshToken = refreshToken;
        this.role = role;
    }

    
}
