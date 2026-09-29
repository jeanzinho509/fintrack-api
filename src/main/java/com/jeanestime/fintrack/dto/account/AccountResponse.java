package com.jeanestime.fintrack.dto.account;

import com.jeanestime.fintrack.entity.AccountType;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record AccountResponse(
        Long id,
        Long userId,
        String name,
        AccountType type,
        BigDecimal initialBalance,
        OffsetDateTime createdAt
) {
}