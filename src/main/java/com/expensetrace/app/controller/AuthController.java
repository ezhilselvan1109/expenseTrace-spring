package com.expensetrace.app.controller;

import com.expensetrace.app.model.User;
import com.expensetrace.app.repository.UserRepository;
import com.expensetrace.app.dto.request.LoginRequestDto;
import com.expensetrace.app.response.ApiResponse;
import com.expensetrace.app.service.user.UserService;
import com.expensetrace.app.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/auth")
@Tag(name = "Auth", description = "Authentication operations including login and logout")
public class AuthController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "User login", description = "Authenticates user and sets JWT in HttpOnly cookie.")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        User user = userRepository.findAll().stream()
                .filter(u -> u.getEmail().equals(loginRequestDto.getEmail()) &&
                        u.getPassword().equals(loginRequestDto.getPassword()))
                .findFirst()
                .orElse(null);

        if (user == null) {
            return ResponseEntity.badRequest().body(new ApiResponse("Invalid credentials", null));
        }

        String token = jwtUtil.generateToken(user.getEmail());

        String cookie = "jwt=" + token +
                "; Path=/" +
                "; HttpOnly" +
                "; Secure" +
                "; SameSite=None" +
                "; Max-Age=" + (24 * 60 * 60);

        response.setHeader("Set-Cookie", cookie);
        return ResponseEntity.ok(new ApiResponse("Login successful", true));
    }

    @Operation(summary = "User logout", description = "Clears JWT cookie to log out the user.")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", "");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ResponseEntity.ok(new ApiResponse("Logout successful", true));
    }

    @Operation(summary = "User forgot-password", description = "forgot-password")
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse> forgotPassword(HttpServletResponse response) {
        return ResponseEntity.ok(new ApiResponse("successful", true));
    }

    @Operation(summary = "User verify-otp", description = "verify-otp")
    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse> verifyOtp(HttpServletResponse response) {
        return ResponseEntity.ok(new ApiResponse("successful", true));
    }

    @GetMapping("/verify")
    public ResponseEntity<ApiResponse> verifyUser(@RequestParam("token") String token) {
        boolean verified = userService.verifyUser(token);
        if (verified) {
            return ResponseEntity.ok(new ApiResponse("Account verified successfully", null));
        } else {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Invalid or expired token", null));
        }
    }

}
