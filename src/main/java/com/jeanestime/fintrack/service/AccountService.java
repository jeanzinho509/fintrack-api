package com.jeanestime.fintrack.service;

import com.jeanestime.fintrack.dto.account.AccountResponse;
import com.jeanestime.fintrack.dto.account.CreateAccountRequest;
import com.jeanestime.fintrack.entity.Account;
import com.jeanestime.fintrack.entity.User;
import com.jeanestime.fintrack.exception.ResourceNotFoundException;
import com.jeanestime.fintrack.repository.AccountRepository;
import com.jeanestime.fintrack.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountService(
            AccountRepository accountRepository,
            UserRepository userRepository
    ) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public AccountResponse create(CreateAccountRequest request) {

        User user = userRepository.findById(request.userId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + request.userId()
                        )
                );

        Account account = new Account(
                user,
                request.name().trim(),
                request.type(),
                request.initialBalance()
        );

        Account savedAccount = accountRepository.save(account);

        return toResponse(savedAccount);
    }

    @Transactional(readOnly = true)
    public AccountResponse findById(Long id) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account not found with id: " + id
                        )
                );

        return toResponse(account);
    }

    @Transactional(readOnly = true)
    public List<AccountResponse> findByUserId(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException(
                    "User not found with id: " + userId
            );
        }

        return accountRepository
                .findAllByUser_IdOrderByCreatedAtAsc(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private AccountResponse toResponse(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getUser().getId(),
                account.getName(),
                account.getType(),
                account.getInitialBalance(),
                account.getCreatedAt()
        );
    }
}