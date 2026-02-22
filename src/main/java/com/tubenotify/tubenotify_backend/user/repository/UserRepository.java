package com.tubenotify.tubenotify_backend.user.repository;

import com.tubenotify.tubenotify_backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for managing user entities
 */
public interface UserRepository extends JpaRepository<User, Long> {
}
