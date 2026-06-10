package com.tubenotify.tubenotify_backend.security.user;

import com.tubenotify.tubenotify_backend.user.entity.User;
import com.tubenotify.tubenotify_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service for loading user details during Spring Security authentication
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid credentials"));

        return AuthUserDetails.buildUserDetails(user);
    }
}
