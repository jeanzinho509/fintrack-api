package com.jeanestime.fintrack;

import java.util.UUID;
import com.jeanestime.fintrack.entity.Account;
import com.jeanestime.fintrack.entity.AccountType;
import com.jeanestime.fintrack.entity.FinancialTransaction;
import com.jeanestime.fintrack.entity.TransactionType;
import com.jeanestime.fintrack.entity.User;
import com.jeanestime.fintrack.repository.AccountRepository;
import com.jeanestime.fintrack.repository.FinancialTransactionRepository;
import com.jeanestime.fintrack.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PersistenceIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private FinancialTransactionRepository transactionRepository;

   @Test
        void shouldPersistUserAccountAndTransaction() {

                String testEmail =
            "persistence-test-" + UUID.randomUUID() + "@example.com";

                User user = new User(
            "Integration Test User",
            testEmail
                );

                User savedUser = userRepository.save(user);

                assertNotNull(savedUser.getId());

                Account account = new Account(
                        savedUser,
                        "Main Account",
                        AccountType.CHECKING,
                        new BigDecimal("1500.00")
                );

         Account savedAccount = accountRepository.save(account);

                assertNotNull(savedAccount.getId());
                assertEquals(savedUser.getId(), savedAccount.getUser().getId());

                FinancialTransaction transaction =
                new FinancialTransaction(
                        savedAccount,
                        TransactionType.EXPENSE,
                        new BigDecimal("89.90"),
                        "Internet bill",
                        OffsetDateTime.now()
                );

                FinancialTransaction savedTransaction =
                 transactionRepository.save(transaction);

                assertNotNull(savedTransaction.getId());
                assertEquals(
                        new BigDecimal("89.90"),
                        savedTransaction.getAmount()
                );

                assertTrue(
                userRepository.findByEmail(
                        testEmail
                ).isPresent()
                );

                assertEquals(
                        1,
                 accountRepository
                        .findAllByUser_IdOrderByCreatedAtAsc(
                                savedUser.getId()
                        )
                        .size()
                 );

                assertEquals(
                        1,
                        transactionRepository
                        .findAllByAccount_IdOrderByOccurredAtDesc(
                                savedAccount.getId()
                        )
                        .size()
                );
        }
}