package com.expensetrace.app.controller;

import com.expensetrace.app.exception.AlreadyExistsException;
import com.expensetrace.app.exception.ResourceNotFoundException;
import com.expensetrace.app.dto.request.UserRequestDto;
import com.expensetrace.app.response.ApiResponse;
import com.expensetrace.app.dto.response.UserResponseDto;
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
@Tag(name = "User", description = "Endpoints for managing users")
public class UserController {
    private final IUserService userService;

    @Operation(summary = "Create a new user", description = "Adds a new user. Throws error if email already exists.")
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        try {
            UserResponseDto newUser = userService.addUser(userRequestDto);
            return ResponseEntity.ok(new ApiResponse("Success", newUser));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @Operation(summary = "Get current user", description = "Returns user details from JWT token context.")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getMe() {
        try {
            UserResponseDto currentUser = userService.getUser();
            return ResponseEntity.ok(new ApiResponse("Found", currentUser));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @Operation(summary = "Delete a user by ID", description = "Deletes user by ID.")
    @DeleteMapping("/user/{id}/delete")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable UUID id) {
        try {
            userService.deleteUserById(id);
            return ResponseEntity.ok(new ApiResponse("Deleted successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @Operation(summary = "Update user", description = "Updates existing user info by ID.")
    @PutMapping("/user/{id}/update")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable UUID id,
                                                  @RequestBody UserRequestDto userRequestDto) {
        try {
            UserResponseDto updatedUser = userService.updateUser(userRequestDto, id);
            return ResponseEntity.ok(new ApiResponse("Update success!", updatedUser));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
