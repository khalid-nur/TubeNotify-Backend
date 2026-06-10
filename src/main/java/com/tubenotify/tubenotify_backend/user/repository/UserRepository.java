package com.tubenotify.tubenotify_backend.user.repository;

import com.tubenotify.tubenotify_backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for managing user entities
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Checks if a user exists with the given email address
     *
     * @param email the email address to check
     * @return true if a user exists with the given email, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Finds a user by their email address
     *
     * @param email the email address to search for
     * @return an Optional containing the user if found, or empty if not found
     */
    Optional<User> findByEmail(String email);
}
