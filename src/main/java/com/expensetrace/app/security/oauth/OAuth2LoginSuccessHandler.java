package com.expensetrace.app.security.oauth;

import com.expensetrace.app.model.User;
import com.expensetrace.app.repository.UserRepository;
import com.expensetrace.app.service.account.IAccountService;
import com.expensetrace.app.category.service.service.ICategoryService;
import com.expensetrace.app.service.settings.ISettingsService;
import com.expensetrace.app.util.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final ISettingsService settingsService;
    private final ICategoryService categoryService;
    private final IAccountService accountService;

    @Value("${frontend.url}")
    private String frontendUrl;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        if (email == null) {
            System.err.println("OAuth2 login failed: no email in Google response. Attributes: "
                    + oAuth2User.getAttributes());
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Google account has no email");
            return;
        }

        User user = userRepository.findByEmail(email);
        if (user == null) {
            user = new User();
            user.setEmail(email);
            user.setName(name != null ? name : "Unknown User");
            User savedUser = userRepository.save(user);
            settingsService.addSettings(savedUser);
            categoryService.createDefaultCategoriesForUser(savedUser);
            accountService.addCashAccount(savedUser);
            System.out.println("New user created: " + email);
        } else {
            System.out.println("Existing user logged in: " + email);
        }

        String token = jwtUtil.generateToken(user.getEmail());

        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60); // 1 day
        response.addCookie(cookie);
        response.sendRedirect(frontendUrl);
    }
}
