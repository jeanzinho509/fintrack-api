package com.jeanestime.fintrack.service;

import com.jeanestime.fintrack.dto.user.CreateUserRequest;
import com.jeanestime.fintrack.dto.user.UserResponse;
import com.jeanestime.fintrack.entity.User;
import com.jeanestime.fintrack.exception.DuplicateResourceException;
import com.jeanestime.fintrack.exception.ResourceNotFoundException;
import com.jeanestime.fintrack.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserResponse create(CreateUserRequest request) {

        String normalizedEmail =
                request.email().trim().toLowerCase();

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new DuplicateResourceException(
                    "A user with this email already exists"
            );
        }

        User user = new User(
                request.name().trim(),
                normalizedEmail
        );

        User savedUser = userRepository.save(user);

        return toResponse(savedUser);
    }

    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + id
                        )
                );

        return toResponse(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {

        return userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }
}