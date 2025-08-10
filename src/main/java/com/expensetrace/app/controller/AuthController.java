package com.expensetrace.app.controller;

import com.expensetrace.app.model.User;
import com.expensetrace.app.repository.UserRepository;
import com.expensetrace.app.dto.request.LoginRequestDto;
import com.expensetrace.app.response.ApiResponse;
import com.expensetrace.app.service.user.UserService;
import com.expensetrace.app.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/auth")
@Tag(name = "Auth", description = "Authentication and password management")
public class AuthController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "User login")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        User user = userRepository.findByEmail(loginRequestDto.getEmail());

        if (user == null) {
            return ResponseEntity.badRequest().body(new ApiResponse("Invalid credentials", null));
        }

        String token = jwtUtil.generateToken(user.getEmail(), 86400000);
        String cookie = "jwt=" + token + "; Path=/; HttpOnly; Secure; SameSite=None; Max-Age=" + (24 * 60 * 60);
        response.setHeader("Set-Cookie", cookie);

        return ResponseEntity.ok(new ApiResponse("Login successful", true));
    }

    @Operation(summary = "User logout")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(HttpServletResponse response) {
        String cookie = "jwt=; Path=/; HttpOnly; Secure; SameSite=None; Max-Age=" + (0);
        response.setHeader("Set-Cookie", cookie);
        return ResponseEntity.ok(new ApiResponse("Logout successful", true));
    }

    @Operation(summary = "Request password reset (send OTP)")
    @PostMapping("/password/forgot")
    public ResponseEntity<ApiResponse> forgotPassword(@RequestParam String email, HttpServletResponse response) {
        userService.sendForgotPasswordOtp(email);
        String token = jwtUtil.generateToken(email, 10 * 60 * 1000);
        String cookie = "reset_token=" + token + "; Path=/; HttpOnly; Secure; SameSite=None; Max-Age=" + (10 * 60);
        response.setHeader("Set-Cookie", cookie);
        return ResponseEntity.ok(new ApiResponse("OTP sent successfully", true));
    }

    @Operation(summary = "Verify OTP")
    @PreAuthorize("hasRole('ROLE_RESET')")
    @PostMapping("/password/verify-otp")
    public ResponseEntity<ApiResponse> verifyOtp(@RequestParam String otp) {
        try {
            boolean isValid = userService.verifyOtp(otp);
            return ResponseEntity.ok(new ApiResponse("OTP verified", isValid));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @Operation(summary = "Reset password")
    @PreAuthorize("hasRole('ROLE_RESET')")
    @PostMapping("/password/reset")
    public ResponseEntity<ApiResponse> resetPassword(@RequestParam String password, HttpServletResponse response) {
        userService.resetPassword(password);
        String cookie = "reset_token=; Path=/; HttpOnly; Secure; SameSite=None; Max-Age=" + (0);
        response.setHeader("Set-Cookie", cookie);
        return ResponseEntity.ok(new ApiResponse("Password reset successfully", true));
    }

    @Operation(summary = "Verify account email")
    @GetMapping("/verify-account")
    public ResponseEntity<ApiResponse> verifyUser(@RequestParam("token") String token) {
        boolean verified = userService.verifyUser(token);
        if (verified) {
            return ResponseEntity.ok(new ApiResponse("Account verified successfully", null));
        } else {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Invalid or expired token", null));
        }
    }
}
