package com.jeanestime.fintrack.controller;

import com.jeanestime.fintrack.dto.account.AccountResponse;
import com.jeanestime.fintrack.dto.account.CreateAccountRequest;
import com.jeanestime.fintrack.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountResponse> create(
            @Valid @RequestBody CreateAccountRequest request
    ) {

        AccountResponse createdAccount =
                accountService.create(request);

        URI location = URI.create(
                "/api/accounts/" + createdAccount.id()
        );

        return ResponseEntity
                .created(location)
                .body(createdAccount);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> findById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                accountService.findById(id)
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AccountResponse>> findByUserId(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(
                accountService.findByUserId(userId)
        );
    }
}