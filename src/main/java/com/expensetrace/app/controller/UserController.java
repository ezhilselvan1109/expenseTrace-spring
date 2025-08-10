package com.expensetrace.app.controller;

import com.expensetrace.app.exception.AlreadyExistsException;
import com.expensetrace.app.exception.ResourceNotFoundException;
import com.expensetrace.app.dto.request.UserRequestDto;
import com.expensetrace.app.dto.response.UserResponseDto;
import com.expensetrace.app.response.ApiResponse;
import com.expensetrace.app.service.user.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/users")
@Tag(name = "User", description = "User management operations")
public class UserController {

    private final IUserService userService;

    @Operation(summary = "Register new user")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        try {
            UserResponseDto newUser = userService.addUser(userRequestDto);
            return ResponseEntity.ok(new ApiResponse("User registered successfully", newUser));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @Operation(summary = "Get current logged-in user")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getMe() {
        try {
            UserResponseDto currentUser = userService.getUser();
            return ResponseEntity.ok(new ApiResponse("Found", currentUser));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @Operation(summary = "Delete a user by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable UUID id) {
        try {
            userService.deleteUserById(id);
            return ResponseEntity.ok(new ApiResponse("Deleted successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @Operation(summary = "Update user by ID")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable UUID id, @Valid @RequestBody UserRequestDto userRequestDto) {
        try {
            UserResponseDto updatedUser = userService.updateUser(userRequestDto);
            return ResponseEntity.ok(new ApiResponse("Updated successfully", updatedUser));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
