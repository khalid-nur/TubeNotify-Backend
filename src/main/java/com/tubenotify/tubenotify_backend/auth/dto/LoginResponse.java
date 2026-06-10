package com.tubenotify.tubenotify_backend.auth.dto;

import com.tubenotify.tubenotify_backend.user.dto.UserDto;
import lombok.Data;

/**
 * Response returned after a successful login containing the access token and user details
 */
@Data
public class LoginResponse {
    private String accessToken;
    private UserDto user;
}
