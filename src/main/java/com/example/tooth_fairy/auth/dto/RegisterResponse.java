package com.example.tooth_fairy.auth.dto;

public record RegisterResponse(
        Long id,
        String email,
        String roles,
        Long accountId,
        String message
) {
}
