package com.tubenotify.tubenotify_backend.auth.mapper;

import com.tubenotify.tubenotify_backend.auth.dto.RegisterRequest;
import com.tubenotify.tubenotify_backend.user.dto.UserDto;
import com.tubenotify.tubenotify_backend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting authentication related objects
 */
@Component
@RequiredArgsConstructor
public class AuthMapper {

    private final ModelMapper modelMapper;

    // Maps RegisterRequest DTO to User entity
    public User mapRegisterRequestToUser(RegisterRequest registerRequest) {
        return modelMapper.map(registerRequest, User.class);
    }

    // Maps User entity to UserDto
    public UserDto mapUserToUserDto(User user) {
        UserDto dto = modelMapper.map(user, UserDto.class);

        if (user.getRole() != null && user.getRole().getRoleName() != null) {
            dto.setRole(user.getRole().getRoleName().name());
        }
        if (user.getAuthProvider() != null) {
            dto.setAuthProvider(user.getAuthProvider().name());
        }

        return dto;
    }
}
