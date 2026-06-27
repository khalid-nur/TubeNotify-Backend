package com.tubenotify.tubenotify_backend.auth.controller;

import com.tubenotify.tubenotify_backend.auth.dto.LoginRequest;
import com.tubenotify.tubenotify_backend.auth.dto.LoginResponse;
import com.tubenotify.tubenotify_backend.auth.dto.RegisterRequest;
import com.tubenotify.tubenotify_backend.auth.service.AuthService;
import com.tubenotify.tubenotify_backend.common.cookie.RefreshTokenCookieService;
import com.tubenotify.tubenotify_backend.common.response.ApiResponse;
import com.tubenotify.tubenotify_backend.user.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    private  final RefreshTokenCookieService refreshTokenCookieService;

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
                .body(ApiResponse.response(true, "User successfully registered", registeredUser, 201));
    }

    /**
     * Authenticates a user and returns an access token
     *
     * @param loginRequest login request containing user credentials
     * @return ResponseEntity containing ApiResponse with access token and user information
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest loginRequest, HttpServletResponse httpResponse
    ) {

        LoginResponse loginResponse = authService.login(loginRequest,httpResponse);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.response(true, "User successfully login", loginResponse, 200));

    }

    /**
     * Refreshes the access token using the refresh token stored in the HTTP cookie
     *
     * @param request the incoming HTTP request containing the refresh token cookie
     * @return ResponseEntity containing ApiResponse with a new access token and user information
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<LoginResponse>> refreshAccessToken(
            HttpServletRequest request
    ) {

        String refreshToken =
                refreshTokenCookieService
                        .getRefreshTokenFromCookies(request);

        LoginResponse loginResponse =
                authService.refreshAccessToken(refreshToken);

        return ResponseEntity.ok(
                ApiResponse.response(
                        true,
                        "Access token refreshed successfully",
                        loginResponse,
                        200
                )
        );
    }
}
