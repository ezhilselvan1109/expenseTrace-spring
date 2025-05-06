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
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/users")
@Tag(name = "User", description = "Manage your User")
public class UserController {
    private final IUserService userService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllUsers() {
        try {
            List<UserResponseDto> userRequestDto = userService.getAllUsers();
            return  ResponseEntity.ok(new ApiResponse("Found!", userRequestDto));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error:", INTERNAL_SERVER_ERROR));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        try {
            UserResponseDto theUser = userService.addUser(userRequestDto);
            return  ResponseEntity.ok(new ApiResponse("Success", theUser));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @Operation(summary = "Get user by ID", description = "Returns a user by their ID")
    @GetMapping("/user/id/{id}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long id){
        try {
            UserResponseDto theUser = userService.getUserById(id);
            return  ResponseEntity.ok(new ApiResponse("Found", theUser));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/user/name/{name}")
    public ResponseEntity<ApiResponse> getUserByName(@PathVariable String name){
        try {
            UserResponseDto theUser = userService.getUserByName(name);
            return  ResponseEntity.ok(new ApiResponse("Found", theUser));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }


    @DeleteMapping("/user/{id}/delete")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long id){
        try {
            userService.deleteUserById(id);
            return  ResponseEntity.ok(new ApiResponse("Found", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

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
