package com.jeanestime.fintrack.service;

import com.jeanestime.fintrack.dto.user.CreateUserRequest;
import com.jeanestime.fintrack.dto.user.UserResponse;
import com.jeanestime.fintrack.entity.User;
import com.jeanestime.fintrack.exception.DuplicateResourceException;
import com.jeanestime.fintrack.exception.ResourceNotFoundException;
import com.jeanestime.fintrack.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    void shouldCreateUser() {

        CreateUserRequest request =
                new CreateUserRequest(
                        " Jean Woodly ",
                        " JEAN@EXAMPLE.COM "
                );

        when(userRepository.existsByEmail("jean@example.com"))
                .thenReturn(false);

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserResponse response = userService.create(request);

        assertEquals("Jean Woodly", response.name());
        assertEquals("jean@example.com", response.email());

        verify(userRepository)
                .existsByEmail("jean@example.com");

        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldRejectDuplicateEmail() {

        CreateUserRequest request =
                new CreateUserRequest(
                        "Jean",
                        "jean@example.com"
                );

        when(userRepository.existsByEmail("jean@example.com"))
                .thenReturn(true);

        assertThrows(
                DuplicateResourceException.class,
                () -> userService.create(request)
        );

        verify(userRepository, never())
                .save(any(User.class));
    }

    @Test
    void shouldThrowWhenUserDoesNotExist() {

        when(userRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> userService.findById(999L)
        );
    }
}