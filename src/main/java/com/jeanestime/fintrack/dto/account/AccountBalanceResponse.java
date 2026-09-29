package com.jeanestime.fintrack.dto.account;

import java.math.BigDecimal;

public record AccountBalanceResponse(
        Long accountId,
        BigDecimal initialBalance,
        BigDecimal totalIncome,
        BigDecimal totalExpense,
        BigDecimal currentBalance
) {
}