package com.jeanestime.fintrack.dto.user;

import java.time.OffsetDateTime;

public record UserResponse(
        Long id,
        String name,
        String email,
        OffsetDateTime createdAt
) {
}