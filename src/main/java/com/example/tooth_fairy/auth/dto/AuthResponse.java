package com.example.tooth_fairy.auth.dto;

public record AuthResponse(
        String token,
        String tokenType,
        Long accountId
) {
}
