package com.tubenotify.tubenotify_backend.user.mapper;

import com.tubenotify.tubenotify_backend.user.dto.UserDto;
import com.tubenotify.tubenotify_backend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting user related objects
 */
@Component
@RequiredArgsConstructor
public class UserMapper {

    private final ModelMapper modelMapper;

    // Maps a User entity to a UserDto
    public UserDto toDto(User user) {
        UserDto dto = modelMapper.map(user, UserDto.class);

        if (user.getRole() != null) {
            dto.setRole(user.getRole().getRoleName().name());
        }

        if (user.getAuthProvider() != null) {
            dto.setAuthProvider(user.getAuthProvider().name());
        }

        return dto;
    }
}
