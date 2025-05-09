package com.expensetrace.app.controller;

import com.expensetrace.app.requestDto.UserRequestDto;
import com.expensetrace.app.exception.AlreadyExistsException;
import com.expensetrace.app.exception.ResourceNotFoundException;
import com.expensetrace.app.response.ApiResponse;
import com.expensetrace.app.responseDto.UserResponseDto;
import com.expensetrace.app.service.user.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/users")
@Tag(name = "User", description = "Endpoints for managing users")
public class UserController {
    private final IUserService userService;

    @Operation(
            summary = "Create a new user",
            description = "Adds a new user to the system. Throws an error if the user already exists."
    )
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        try {
            UserResponseDto theUser = userService.addUser(userRequestDto);
            return  ResponseEntity.ok(new ApiResponse("Success", theUser));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @Operation(
            summary = "Retrieve a user by ID",
            description = "Fetches a user from the database using the provided user ID."
    )
    @GetMapping("/user/{id}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long id){
        try {
            UserResponseDto theUser = userService.getUserById(id);
            return  ResponseEntity.ok(new ApiResponse("Found", theUser));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @Operation(
            summary = "Delete a user by ID",
            description = "Removes a user from the system based on the provided user ID."
    )
    @DeleteMapping("/user/{id}/delete")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long id){
        try {
            userService.deleteUserById(id);
            return  ResponseEntity.ok(new ApiResponse("Deleted successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @Operation(
            summary = "Update an existing user",
            description = "Updates user information for the specified ID with the provided details."
    )
    @PutMapping("/user/{id}/update")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable Long id, @RequestBody UserRequestDto userRequestDto) {
        try {
            UserResponseDto updatedUser = userService.updateUser(userRequestDto, id);
            return ResponseEntity.ok(new ApiResponse("Update success!", updatedUser));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
