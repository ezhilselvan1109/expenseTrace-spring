package com.expensetrace.app.controller;

import com.expensetrace.app.model.User;
import com.expensetrace.app.repository.UserRepository;
import com.expensetrace.app.requestDto.LoginRequestDto;
import com.expensetrace.app.response.ApiResponse;
import com.expensetrace.app.util.JwtUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/auth")
@Tag(name = "auth", description = "Manage your auth")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequestDto loginRequestDto,
                                             HttpServletResponse response) {
        User user = userRepository.findAll().stream()
                .filter(u -> u.getEmail().equals(loginRequestDto.getEmail()) &&
                        u.getPassword().equals(loginRequestDto.getPassword()))
                .findFirst()
                .orElse(null);

        if (user == null) {
            return ResponseEntity.badRequest().body(new ApiResponse("Invalid credentials", null));
        }

        String token = jwtUtil.generateToken(user.getEmail());

        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // use only over HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60);

        response.addCookie(cookie);
        return ResponseEntity.ok(new ApiResponse("Login successful", null));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", "");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ResponseEntity.ok(new ApiResponse("Logout successful", null));
    }

}
