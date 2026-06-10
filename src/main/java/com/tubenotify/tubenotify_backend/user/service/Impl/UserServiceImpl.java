package com.tubenotify.tubenotify_backend.user.service.Impl;

import com.tubenotify.tubenotify_backend.common.exception.InvalidCredentialsException;
import com.tubenotify.tubenotify_backend.user.dto.UserDto;
import com.tubenotify.tubenotify_backend.user.entity.User;
import com.tubenotify.tubenotify_backend.user.mapper.UserMapper;
import com.tubenotify.tubenotify_backend.user.repository.UserRepository;
import com.tubenotify.tubenotify_backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service implementation for handling user operations
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));

        return userMapper.toDto(user);
    }
}