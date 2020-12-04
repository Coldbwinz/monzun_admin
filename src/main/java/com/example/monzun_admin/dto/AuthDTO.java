package com.example.monzun_admin.dto;

public class AuthDTO {
    private final String token;

    public AuthDTO(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
