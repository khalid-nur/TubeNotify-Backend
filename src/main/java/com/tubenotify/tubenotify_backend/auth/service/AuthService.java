package com.tubenotify.tubenotify_backend.auth.service;

import com.tubenotify.tubenotify_backend.auth.dto.RegisterRequest;
import com.tubenotify.tubenotify_backend.user.dto.UserDto;

/**
 * Service interface for handling authentication
 */
public interface AuthService {

    /**
     * Registers a new user account.
     *
     * @param request registration request containing user details
     * @return UserDto containing registered user information
     */
    UserDto register(RegisterRequest request);

}

