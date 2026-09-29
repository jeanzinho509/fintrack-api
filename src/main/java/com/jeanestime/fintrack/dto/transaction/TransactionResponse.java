package com.jeanestime.fintrack.dto.transaction;

import com.jeanestime.fintrack.entity.TransactionType;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record TransactionResponse(
        Long id,
        Long accountId,
        TransactionType type,
        BigDecimal amount,
        String description,
        OffsetDateTime occurredAt,
        OffsetDateTime createdAt
) {
}