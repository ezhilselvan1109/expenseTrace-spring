package com.expensetrace.app.controller;

import com.expensetrace.app.requestDto.AccountRequestDto;
import com.expensetrace.app.exception.AlreadyExistsException;
import com.expensetrace.app.exception.ResourceNotFoundException;
import com.expensetrace.app.response.ApiResponse;
import com.expensetrace.app.responseDto.AccountResponseDto;
import com.expensetrace.app.service.account.IAccountService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/accounts")
@Tag(name = "Account", description = "Manage your Account")
public class AccountController {
    private final IAccountService accountService;

    @GetMapping("/default-payment-mode")
    public ResponseEntity<ApiResponse> getDefaultPaymentMode() {
        Long userId = 1L; // Replace with real user auth context
        try {
            AccountResponseDto defaultAccount = accountService.getDefaultPaymentModeByUserId(userId);
            return ResponseEntity.ok(new ApiResponse("Found!", defaultAccount));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{id}/default-payment-mode")
    public ResponseEntity<ApiResponse> updateDefaultPaymentMode(@PathVariable Long id) {
        Long userId = 1L; // Replace with real user auth context
        try {
            AccountResponseDto updated = accountService.updateDefaultPaymentMode(id, userId);
            return ResponseEntity.ok(new ApiResponse("Update success!", updated));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }



    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllAccounts() {
        try {
            List<AccountResponseDto> accountsResponseDto = accountService.getAllAccounts();
            return ResponseEntity.ok(new ApiResponse("Found!", accountsResponseDto));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error: " + e.getMessage(), null));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addAccount(@RequestBody AccountRequestDto requestDto) {
        Long userId = 1L; // 🔐 Replace with actual authenticated user ID
        try {
            AccountResponseDto response = accountService.addAccount(requestDto, userId);
            return ResponseEntity.status(CREATED).body(new ApiResponse("Success", response));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/account/{id}")
    public ResponseEntity<ApiResponse> getAccountById(@PathVariable Long id) {
        try {
            AccountResponseDto response = accountService.getAccountById(id);
            return ResponseEntity.ok(new ApiResponse("Found", response));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/account/name/{name}")
    public ResponseEntity<ApiResponse> getAccountByName(@PathVariable String name) {
        try {
            AccountResponseDto response = accountService.getAccountByName(name);
            return ResponseEntity.ok(new ApiResponse("Found", response));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/account/{id}")
    public ResponseEntity<ApiResponse> deleteAccount(@PathVariable Long id) {
        try {
            accountService.deleteAccountById(id);
            return ResponseEntity.ok(new ApiResponse("Deleted successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/account/{id}")
    public ResponseEntity<ApiResponse> updateAccount(@PathVariable Long id, @RequestBody AccountRequestDto accountDto) {
        try {
            AccountResponseDto updated = accountService.updateAccount(accountDto, id);
            return ResponseEntity.ok(new ApiResponse("Update success!", updated));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}