package com.tubenotify.tubenotify_backend.auth.service.Impl;


import com.tubenotify.tubenotify_backend.auth.dto.LoginRequest;
import com.tubenotify.tubenotify_backend.auth.dto.LoginResponse;
import com.tubenotify.tubenotify_backend.auth.dto.RegisterRequest;
import com.tubenotify.tubenotify_backend.auth.mapper.AuthMapper;
import com.tubenotify.tubenotify_backend.auth.service.AuthService;
import com.tubenotify.tubenotify_backend.common.cookie.RefreshTokenCookieService;
import com.tubenotify.tubenotify_backend.common.exception.InvalidCredentialsException;
import com.tubenotify.tubenotify_backend.common.exception.UserAlreadyExistsException;
import com.tubenotify.tubenotify_backend.security.jwt.JwtTokenService;
import com.tubenotify.tubenotify_backend.security.user.AuthUserDetails;
import com.tubenotify.tubenotify_backend.security.user.AuthUserDetailsService;
import com.tubenotify.tubenotify_backend.user.dto.UserDto;
import com.tubenotify.tubenotify_backend.user.entity.Role;
import com.tubenotify.tubenotify_backend.user.entity.User;
import com.tubenotify.tubenotify_backend.user.enums.AuthProvider;
import com.tubenotify.tubenotify_backend.user.enums.UserRole;
import com.tubenotify.tubenotify_backend.user.repository.RoleRepository;
import com.tubenotify.tubenotify_backend.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service implementation for handling authentication business logic
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthMapper authMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final RefreshTokenCookieService refreshTokenCookieService;
    private final AuthUserDetailsService authUserDetailsService;



    @Override
    public UserDto register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("An account with this email already exists");
        }

        User user = authMapper.mapRegisterRequestToUser(request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));


        Role role = roleRepository.findByRoleName(UserRole.ROLE_USER)
                .orElseGet(() -> roleRepository.save(new Role(UserRole.ROLE_USER)));

        user.setRole(role);
        user.setAuthProvider(AuthProvider.EMAIL);

        User saved = userRepository.save(user);
        return authMapper.mapUserToUserDto(saved);
    }

    @Override
    public LoginResponse login(LoginRequest authRequest, HttpServletResponse httpResponse) {

        Authentication authentication = authenticate(authRequest);

        AuthUserDetails user = (AuthUserDetails) authentication.getPrincipal();

        User existingUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        // Check if the user is trying to log in with email/password but their account is linked to Google
        if (existingUser.getAuthProvider() == AuthProvider.GOOGLE) {
            throw new InvalidCredentialsException(
                    "This account uses Google login. Please sign in with Google."
            );
        }

        UserDto userDto = authMapper.mapUserToUserDto(existingUser);

        String token = jwtTokenService.generateToken(user);

        String refreshToken = jwtTokenService.generateRefreshToken(user);

        refreshTokenCookieService.addRefreshTokenCookie(httpResponse, refreshToken);

        LoginResponse response = new LoginResponse();
        response.setAccessToken(token);
        response.setUser(userDto);

        return response;
    }

    @Override
    public LoginResponse refreshAccessToken(String refreshToken) {

        if (refreshToken == null || refreshToken.isBlank()) {

            throw new InvalidCredentialsException(
                    "Refresh token is missing"
            );
        }

        if (!jwtTokenService.validateToken(refreshToken)) {

            throw new InvalidCredentialsException(
                    "Invalid or expired refresh token"
            );
        }

        String email = jwtTokenService.getUsernameFromToken(refreshToken);

        UserDetails userDetails =
                authUserDetailsService.loadUserByUsername(email);

        User userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> {

                    return new InvalidCredentialsException(
                            "User not found"
                    );
                });

        UserDto userDto = authMapper.mapUserToUserDto(userEntity);

        String newAccessToken =
                jwtTokenService.generateToken(userDetails);

        LoginResponse response = new LoginResponse();
        response.setAccessToken(newAccessToken);
        response.setUser(userDto);

        return response;
    }

    private Authentication authenticate(LoginRequest loginRequest) {

        try {

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

        } catch (BadCredentialsException ex) {

            log.warn("Authentication failed: {}", ex.getMessage());

            log.warn("Failed login attempt for email: {}", loginRequest.getEmail());

            throw new InvalidCredentialsException("Invalid email or password");
        }
    }
}
