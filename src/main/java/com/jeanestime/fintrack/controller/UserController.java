package com.jeanestime.fintrack.controller;

import com.jeanestime.fintrack.dto.user.CreateUserRequest;
import com.jeanestime.fintrack.dto.user.UserResponse;
import com.jeanestime.fintrack.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(
            @Valid @RequestBody CreateUserRequest request
    ) {

        UserResponse createdUser = userService.create(request);

        URI location = URI.create(
                "/api/users/" + createdUser.id()
        );

        return ResponseEntity
                .created(location)
                .body(createdUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                userService.findById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll() {
        return ResponseEntity.ok(
                userService.findAll()
        );
    }
}