package com.jeanestime.fintrack.service;

import com.jeanestime.fintrack.dto.account.AccountBalanceResponse;
import com.jeanestime.fintrack.dto.account.CreateAccountRequest;
import com.jeanestime.fintrack.entity.Account;
import com.jeanestime.fintrack.entity.AccountType;
import com.jeanestime.fintrack.entity.TransactionType;
import com.jeanestime.fintrack.entity.User;
import com.jeanestime.fintrack.exception.ResourceNotFoundException;
import com.jeanestime.fintrack.repository.AccountRepository;
import com.jeanestime.fintrack.repository.FinancialTransactionRepository;
import com.jeanestime.fintrack.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AccountServiceTest {

    private AccountRepository accountRepository;
    private UserRepository userRepository;
    private FinancialTransactionRepository transactionRepository;

    private AccountService accountService;

    @BeforeEach
    void setUp() {
        accountRepository = mock(AccountRepository.class);
        userRepository = mock(UserRepository.class);
        transactionRepository =
                mock(FinancialTransactionRepository.class);

        accountService = new AccountService(
                accountRepository,
                userRepository,
                transactionRepository
        );
    }

    @Test
    void shouldCreateAccount() {

        User user =
                new User("Jean", "jean@example.com");

        CreateAccountRequest request =
                new CreateAccountRequest(
                        1L,
                        " Main Account ",
                        AccountType.CHECKING,
                        new BigDecimal("1500.00")
                );

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        var response = accountService.create(request);

        assertEquals("Main Account", response.name());
        assertEquals(AccountType.CHECKING, response.type());

        assertEquals(
                new BigDecimal("1500.00"),
                response.initialBalance()
        );

        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void shouldCalculateCurrentBalance() {

        User user =
                new User("Jean", "jean@example.com");

        Account account =
                new Account(
                        user,
                        "Main Account",
                        AccountType.CHECKING,
                        new BigDecimal("1500.00")
                );

        when(accountRepository.findById(5L))
                .thenReturn(Optional.of(account));

        when(
                transactionRepository
                        .sumAmountByAccountIdAndType(
                                5L,
                                TransactionType.INCOME
                        )
        ).thenReturn(new BigDecimal("3500.00"));

        when(
                transactionRepository
                        .sumAmountByAccountIdAndType(
                                5L,
                                TransactionType.EXPENSE
                        )
        ).thenReturn(new BigDecimal("250.90"));

        AccountBalanceResponse response =
                accountService.getBalance(5L);

        assertEquals(
                new BigDecimal("3500.00"),
                response.totalIncome()
        );

        assertEquals(
                new BigDecimal("250.90"),
                response.totalExpense()
        );

        assertEquals(
                new BigDecimal("4749.10"),
                response.currentBalance()
        );
    }

    @Test
    void shouldThrowWhenCreatingAccountForMissingUser() {

        when(userRepository.findById(999L))
                .thenReturn(Optional.empty());

        CreateAccountRequest request =
                new CreateAccountRequest(
                        999L,
                        "Main Account",
                        AccountType.CHECKING,
                        BigDecimal.ZERO
                );

        assertThrows(
                ResourceNotFoundException.class,
                () -> accountService.create(request)
        );

        verify(accountRepository, never())
                .save(any(Account.class));
    }
}