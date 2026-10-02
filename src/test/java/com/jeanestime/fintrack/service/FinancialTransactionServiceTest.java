package com.jeanestime.fintrack.service;

import com.jeanestime.fintrack.dto.transaction.CreateTransactionRequest;
import com.jeanestime.fintrack.entity.Account;
import com.jeanestime.fintrack.entity.AccountType;
import com.jeanestime.fintrack.entity.FinancialTransaction;
import com.jeanestime.fintrack.entity.TransactionType;
import com.jeanestime.fintrack.entity.User;
import com.jeanestime.fintrack.exception.ResourceNotFoundException;
import com.jeanestime.fintrack.repository.AccountRepository;
import com.jeanestime.fintrack.repository.FinancialTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FinancialTransactionServiceTest {

    private FinancialTransactionRepository transactionRepository;
    private AccountRepository accountRepository;

    private FinancialTransactionService transactionService;

    @BeforeEach
    void setUp() {

        transactionRepository =
                mock(FinancialTransactionRepository.class);

        accountRepository =
                mock(AccountRepository.class);

        transactionService =
                new FinancialTransactionService(
                        transactionRepository,
                        accountRepository
                );
    }

    @Test
    void shouldCreateTransaction() {

        User user =
                new User("Jean", "jean@example.com");

        Account account =
                new Account(
                        user,
                        "Main Account",
                        AccountType.CHECKING,
                        new BigDecimal("1500.00")
                );

        CreateTransactionRequest request =
                new CreateTransactionRequest(
                        5L,
                        TransactionType.EXPENSE,
                        new BigDecimal("250.90"),
                        "  Groceries  ",
                        null
                );

        when(accountRepository.findById(5L))
                .thenReturn(Optional.of(account));

        when(
                transactionRepository
                        .save(any(FinancialTransaction.class))
        ).thenAnswer(invocation ->
                invocation.getArgument(0));

        var response =
                transactionService.create(request);

        assertEquals(
                TransactionType.EXPENSE,
                response.type()
        );

        assertEquals(
                new BigDecimal("250.90"),
                response.amount()
        );

        assertEquals(
                "Groceries",
                response.description()
        );

        verify(transactionRepository)
                .save(any(FinancialTransaction.class));
    }

    @Test
    void shouldRejectTransactionForMissingAccount() {

        when(accountRepository.findById(999L))
                .thenReturn(Optional.empty());

        CreateTransactionRequest request =
                new CreateTransactionRequest(
                        999L,
                        TransactionType.EXPENSE,
                        new BigDecimal("50.00"),
                        "Test",
                        null
                );

        assertThrows(
                ResourceNotFoundException.class,
                () -> transactionService.create(request)
        );

        verify(transactionRepository, never())
                .save(any(FinancialTransaction.class));
    }

    @Test
    void shouldNormalizeBlankDescriptionToNull() {

        User user =
                new User("Jean", "jean@example.com");

        Account account =
                new Account(
                        user,
                        "Main Account",
                        AccountType.CHECKING,
                        BigDecimal.ZERO
                );

        when(accountRepository.findById(5L))
                .thenReturn(Optional.of(account));

        when(
                transactionRepository
                        .save(any(FinancialTransaction.class))
        ).thenAnswer(invocation ->
                invocation.getArgument(0));

        CreateTransactionRequest request =
                new CreateTransactionRequest(
                        5L,
                        TransactionType.INCOME,
                        new BigDecimal("100.00"),
                        "   ",
                        null
                );

        var response =
                transactionService.create(request);

        assertNull(response.description());
    }
}