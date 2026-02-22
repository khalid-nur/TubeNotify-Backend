package com.tubenotify.tubenotify_backend.user.repository;

import com.tubenotify.tubenotify_backend.user.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for managing role entities
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
}
