package com.jeanestime.fintrack.controller;

import com.jeanestime.fintrack.dto.transaction.CreateTransactionRequest;
import com.jeanestime.fintrack.dto.transaction.TransactionResponse;
import com.jeanestime.fintrack.service.FinancialTransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class FinancialTransactionController {

    private final FinancialTransactionService transactionService;

    public FinancialTransactionController(
            FinancialTransactionService transactionService
    ) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> create(
            @Valid @RequestBody CreateTransactionRequest request
    ) {

        TransactionResponse createdTransaction =
                transactionService.create(request);

        URI location = URI.create(
                "/api/transactions/" + createdTransaction.id()
        );

        return ResponseEntity
                .created(location)
                .body(createdTransaction);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> findById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                transactionService.findById(id)
        );
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<TransactionResponse>> findByAccountId(
            @PathVariable Long accountId
    ) {
        return ResponseEntity.ok(
                transactionService.findByAccountId(accountId)
        );
    }
}