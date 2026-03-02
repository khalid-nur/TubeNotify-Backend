package com.tubenotify.tubenotify_backend.auth.service.Impl;


import com.tubenotify.tubenotify_backend.auth.dto.RegisterRequest;
import com.tubenotify.tubenotify_backend.auth.mapper.AuthMapper;
import com.tubenotify.tubenotify_backend.auth.service.AuthService;
import com.tubenotify.tubenotify_backend.common.exception.UserAlreadyExistsException;
import com.tubenotify.tubenotify_backend.user.dto.UserDto;
import com.tubenotify.tubenotify_backend.user.entity.Role;
import com.tubenotify.tubenotify_backend.user.entity.User;
import com.tubenotify.tubenotify_backend.user.enums.AuthProvider;
import com.tubenotify.tubenotify_backend.user.enums.UserRole;
import com.tubenotify.tubenotify_backend.user.repository.RoleRepository;
import com.tubenotify.tubenotify_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service implementation for handling authentication business logic handling user authentication
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthMapper authMapper;
    private final PasswordEncoder passwordEncoder;


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
}
