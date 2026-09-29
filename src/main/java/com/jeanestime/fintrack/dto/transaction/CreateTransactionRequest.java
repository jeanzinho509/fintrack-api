package com.jeanestime.fintrack.dto.transaction;

import com.jeanestime.fintrack.entity.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record CreateTransactionRequest(

        @NotNull(message = "Account id is required")
        Long accountId,

        @NotNull(message = "Transaction type is required")
        TransactionType type,

        @NotNull(message = "Amount is required")
        @DecimalMin(
                value = "0.01",
                message = "Amount must be greater than zero"
        )
        BigDecimal amount,

        @Size(
                max = 255,
                message = "Description must contain at most 255 characters"
        )
        String description,

        OffsetDateTime occurredAt
) {
}