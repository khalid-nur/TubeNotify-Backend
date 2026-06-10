package com.tubenotify.tubenotify_backend.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tubenotify.tubenotify_backend.common.response.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

/**
 * Entry point for handling unauthorized access attempts
 * Intercepts authentication failures and returns appropriate error responses based on JWT exceptions
 */
@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public JwtAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Handles unauthorized access by returning a 401 response with error details
     *
     * @param request the incoming HTTP request that failed authentication
     * @param response the HTTP response used to send the 401 error back to the client
     * @param authException the exception that triggered the authentication failure
     * @throws IOException if an error occurs while writing the response to the client
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        // Check if a JWT exception was set by JwtAuthenticationFilter
        Throwable jwtEx = (Throwable) request.getAttribute("jwtException");
        String message = "Unauthorized";

        // Set message based on whether the token is expired, invalid, or missing
        if (jwtEx instanceof ExpiredJwtException) {
            message = "Access token has expired";
            log.warn("JWT expired for request [{} {}]", request.getMethod(), request.getRequestURI());
        } else if (jwtEx instanceof JwtException) {
            message = "Invalid access token";
            log.warn("Invalid JWT for request [{} {}]: {}",
                    request.getMethod(),
                    request.getRequestURI(),
                    jwtEx.getMessage());
        } else {
            log.error("Unauthorized access on [{} {}]: {}",
                    request.getMethod(),
                    request.getRequestURI(),
                    authException.getMessage());
        }

        // Build the 401 error response
        ErrorResponse error = ErrorResponse.builder()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .errorCode("UNAUTHORIZED")
                .message(message)
                .timestamp(Instant.now())
                .build();

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");

        // Write the error response to the output stream
        objectMapper.writeValue(response.getOutputStream(), error);
    }
}
