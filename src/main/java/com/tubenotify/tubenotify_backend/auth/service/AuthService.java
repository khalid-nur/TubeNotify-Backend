package com.tubenotify.tubenotify_backend.auth.service;

import com.tubenotify.tubenotify_backend.auth.dto.LoginRequest;
import com.tubenotify.tubenotify_backend.auth.dto.LoginResponse;
import com.tubenotify.tubenotify_backend.auth.dto.RegisterRequest;
import com.tubenotify.tubenotify_backend.user.dto.UserDto;
import jakarta.servlet.http.HttpServletResponse;

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

    /**
     * Authenticates a user and returns an access token
     *
     * @param request login request containing user credentials
     * @return LoginResponse containing access token and user information
     */
    LoginResponse login(LoginRequest request, HttpServletResponse response);

    /**
     * Refreshes the access token using the provided refresh token
     *
     * @param refreshToken the refresh token extracted from the HTTP cookie
     * @return LoginResponse containing a new access token and user information
     */
    LoginResponse refreshAccessToken(String refreshToken);


}

