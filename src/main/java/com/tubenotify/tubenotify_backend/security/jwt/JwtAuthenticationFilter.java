package com.tubenotify.tubenotify_backend.security.jwt;


import com.tubenotify.tubenotify_backend.security.user.AuthUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter for intercepting and validating JWT tokens on every incoming request
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;
    private final AuthUserDetailsService authUserDetailsService;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        log.debug("AuthTokenFilter called for URI: {}", request.getRequestURI());


        try {

            String token = jwtTokenService.extractTokenFromRequest(request);

            if (token != null) {

                String email = jwtTokenService.getUsernameFromToken(token);

                UserDetails userDetails =
                        authUserDetailsService.loadUserByUsername(email);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);

            }

            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException ex) {

            log.warn("Expired JWT token: {}", ex.getMessage());

            request.setAttribute("jwtException", ex);

            jwtAuthenticationEntryPoint.commence(
                    request,
                    response,
                    new AuthenticationException("Token expired") {}
            );

        } catch (JwtException | IllegalArgumentException ex) {

            log.warn("Invalid JWT token: {}", ex.getMessage());

            request.setAttribute("jwtException", ex);

            jwtAuthenticationEntryPoint.commence(
                    request,
                    response,
                    new AuthenticationException("Invalid token") {}
            );
        }

    }
}
