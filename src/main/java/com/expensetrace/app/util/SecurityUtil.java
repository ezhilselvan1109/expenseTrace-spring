package com.expensetrace.app.util;

import com.expensetrace.app.model.User;
import com.expensetrace.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SecurityUtil {

    private final UserRepository userRepository;

    public UUID getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        boolean hasUserRole = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_USER"));

        if (!hasUserRole) {
            return null; // Prevent other roles from accessing
        }

        String email = (String) authentication.getPrincipal();
        User user = userRepository.findByEmail(email);

        return user != null ? user.getId() : null;
    }

    public UUID getResetUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        boolean hasResetRole = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_RESET"));

        if (!hasResetRole) {
            return null;
        }

        String email = (String) authentication.getPrincipal();
        User user = userRepository.findByEmail(email);

        return user != null ? user.getId() : null;
    }
}
