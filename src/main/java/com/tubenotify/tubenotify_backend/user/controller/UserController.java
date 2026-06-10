package com.tubenotify.tubenotify_backend.user.controller;

import com.tubenotify.tubenotify_backend.common.response.ApiResponse;
import com.tubenotify.tubenotify_backend.security.user.AuthUserDetails;
import com.tubenotify.tubenotify_backend.user.dto.UserDto;
import com.tubenotify.tubenotify_backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling user operations
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Retrieves the currently authenticated user's profile
     *
     * @param userDetails the authenticated user's details
     * @return ResponseEntity containing ApiResponse with the current user's information
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDto>> getCurrentUser(@AuthenticationPrincipal AuthUserDetails userDetails) {

        String userEmail = userDetails.getUsername();

        UserDto dto = userService.getUserByEmail(userEmail);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.response(true, "User fetched successfully", dto,
                200));
    }
}
