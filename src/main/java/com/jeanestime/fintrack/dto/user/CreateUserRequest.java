package com.jeanestime.fintrack.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(

        @NotBlank(message = "Name is required")
        @Size(
                min = 2,
                max = 120,
                message = "Name must contain between 2 and 120 characters"
        )
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        @Size(
                max = 180,
                message = "Email must contain at most 180 characters"
        )
        String email
) {
}