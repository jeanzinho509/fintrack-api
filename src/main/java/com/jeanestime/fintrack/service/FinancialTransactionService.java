package com.jeanestime.fintrack.service;

import com.jeanestime.fintrack.dto.transaction.CreateTransactionRequest;
import com.jeanestime.fintrack.dto.transaction.TransactionResponse;
import com.jeanestime.fintrack.entity.Account;
import com.jeanestime.fintrack.entity.FinancialTransaction;
import com.jeanestime.fintrack.exception.ResourceNotFoundException;
import com.jeanestime.fintrack.repository.AccountRepository;
import com.jeanestime.fintrack.repository.FinancialTransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FinancialTransactionService {

    private final FinancialTransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public FinancialTransactionService(
            FinancialTransactionRepository transactionRepository,
            AccountRepository accountRepository
    ) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public TransactionResponse create(
            CreateTransactionRequest request
    ) {

        Account account = accountRepository
                .findById(request.accountId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account not found with id: "
                                        + request.accountId()
                        )
                );

        FinancialTransaction transaction =
                new FinancialTransaction(
                        account,
                        request.type(),
                        request.amount(),
                        normalizeDescription(request.description()),
                        request.occurredAt()
                );

        FinancialTransaction savedTransaction =
                transactionRepository.save(transaction);

        return toResponse(savedTransaction);
    }

    @Transactional(readOnly = true)
    public TransactionResponse findById(Long id) {

        FinancialTransaction transaction =
                transactionRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Transaction not found with id: " + id
                                )
                        );

        return toResponse(transaction);
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> findByAccountId(
            Long accountId
    ) {

        if (!accountRepository.existsById(accountId)) {
            throw new ResourceNotFoundException(
                    "Account not found with id: " + accountId
            );
        }

        return transactionRepository
                .findAllByAccount_IdOrderByOccurredAtDesc(accountId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private TransactionResponse toResponse(
            FinancialTransaction transaction
    ) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getAccount().getId(),
                transaction.getType(),
                transaction.getAmount(),
                transaction.getDescription(),
                transaction.getOccurredAt(),
                transaction.getCreatedAt()
        );
    }

    private String normalizeDescription(String description) {
        if (description == null) {
            return null;
        }

        String normalized = description.trim();

        return normalized.isBlank()
                ? null
                : normalized;
    }
}