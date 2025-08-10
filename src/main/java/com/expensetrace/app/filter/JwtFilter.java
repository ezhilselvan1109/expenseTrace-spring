package com.expensetrace.app.filter;

import com.expensetrace.app.model.User;
import com.expensetrace.app.repository.UserRepository;
import com.expensetrace.app.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    private static final List<String> EXCLUDED_PATHS = Arrays.asList(
            "/", "/index.html", "/swagger-ui.html", "/swagger-ui/**",
            "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**",
            "/api-docs/**", "/api/v1/auth/login", "/api/v1/users/add",
            "/api/v1/auth/verify",
            "/api/v1/auth/password/forgot",
            "/h2-console/**"
    );

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return EXCLUDED_PATHS.stream().anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String loginToken = null;
        String resetToken = null;

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    loginToken = cookie.getValue();
                } else if ("reset_token".equals(cookie.getName())) {
                    resetToken = cookie.getValue();
                }
            }
        }

        try {
            String path = request.getRequestURI();

            // If request is for password reset endpoints → use reset token
            if (path.startsWith("/api/v1/auth/password/")) {
                if (resetToken != null) {
                    authenticateUserFromToken(resetToken, "ROLE_RESET");
                } else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            }
            // Else → use normal login token
            else {
                if (loginToken != null) {
                    authenticateUserFromToken(loginToken, "ROLE_USER");
                } else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void authenticateUserFromToken(String token, String role) {
        String email = jwtUtil.extractEmail(token);
        User user = userRepository.findByEmail(email);

        if (user != null && jwtUtil.validateToken(token, email)) {
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            user.getEmail(), null, Collections.singleton(() -> role));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            throw new RuntimeException("Invalid or expired token");
        }
    }
}
