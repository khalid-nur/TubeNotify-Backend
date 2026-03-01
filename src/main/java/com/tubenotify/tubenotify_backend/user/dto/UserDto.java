package com.tubenotify.tubenotify_backend.user.dto;

import lombok.Data;

import java.time.Instant;

/**
 * Represents a user profile
 */
@Data
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String role;
    private String authProvider;
    private String phoneNumber;
    private boolean phoneVerified;
    private boolean subscribed;
    private Instant createdAt;
    private Instant updatedAt;
}
