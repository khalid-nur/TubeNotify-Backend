package com.tubenotify.tubenotify_backend.user.service;

import com.tubenotify.tubenotify_backend.user.dto.UserDto;

/**
 * Service interface for handling user operations
 */
public interface UserService {

    /**
     * Retrieves a user by their email address
     *
     * @param email the email address of the user to retrieve
     * @return UserDto containing the user's information
     */
    UserDto getUserByEmail (String email);
}

