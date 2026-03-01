package com.tubenotify.tubenotify_backend.user.repository;

import com.tubenotify.tubenotify_backend.user.entity.Role;
import com.tubenotify.tubenotify_backend.user.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for managing role entities
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(UserRole userRole);
}
