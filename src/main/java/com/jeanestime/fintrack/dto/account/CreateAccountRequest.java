package com.jeanestime.fintrack.dto.account;

import com.jeanestime.fintrack.entity.AccountType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateAccountRequest(

        @NotNull(message = "User id is required")
        Long userId,

        @NotBlank(message = "Account name is required")
        @Size(max = 100, message = "Account name must contain at most 100 characters")
        String name,

        @NotNull(message = "Account type is required")
        AccountType type,

        @NotNull(message = "Initial balance is required")
        @DecimalMin(
                value = "0.00",
                inclusive = true,
                message = "Initial balance cannot be negative"
        )
        BigDecimal initialBalance
) {
}