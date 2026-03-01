package com.tubenotify.tubenotify_backend.auth.controller;

import com.tubenotify.tubenotify_backend.auth.dto.RegisterRequest;
import com.tubenotify.tubenotify_backend.auth.service.AuthService;
import com.tubenotify.tubenotify_backend.common.response.ApiResponse;
import com.tubenotify.tubenotify_backend.user.dto.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling authentication operations
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * Registers a new user account
     *
     * @param request registration request containing user details
     * @return ResponseEntity containing ApiResponse with registered user information
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDto>> register(@Valid @RequestBody RegisterRequest request) {
        UserDto registeredUser = authService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.response(true, "User successful registered", registeredUser, 201));
    }
}
