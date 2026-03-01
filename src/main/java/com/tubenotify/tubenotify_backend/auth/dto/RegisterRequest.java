package com.tubenotify.tubenotify_backend.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Validated user registration request details
 */
@Data
public class RegisterRequest {

    @NotBlank(message = "Enter a username to continue")
    private String userName;

    @Email(message = "Please enter a valid email address")
    @NotBlank(message = "Please enter your email address")
    private String email;

    @NotBlank(message = "Please enter a password")
    @Size(min = 6, message ="Please provide a valid password to continue")
    private String password;
}
