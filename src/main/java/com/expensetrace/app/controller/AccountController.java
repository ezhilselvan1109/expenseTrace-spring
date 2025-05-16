package com.expensetrace.app.controller;

import com.expensetrace.app.enums.AccountType;
import com.expensetrace.app.requestDto.AccountRequestDto;
import com.expensetrace.app.exception.AlreadyExistsException;
import com.expensetrace.app.exception.ResourceNotFoundException;
import com.expensetrace.app.response.ApiResponse;
import com.expensetrace.app.responseDto.AccountResponseDto;
import com.expensetrace.app.service.account.IAccountService;
import com.expensetrace.app.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/accounts")
@Tag(name = "Account", description = "Manage user accounts")
public class AccountController {

    private final IAccountService accountService;

    @GetMapping("/default-payment-mode")
    @Operation(summary = "Get default payment mode", description = "Retrieve the default payment mode for the authenticated user")
    public ResponseEntity<ApiResponse> getDefaultPaymentMode() {
        try {
            AccountResponseDto defaultAccount = accountService.getDefaultPaymentModeByUserId();
            return ResponseEntity.ok(new ApiResponse("Found!", defaultAccount));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{id}/default-payment-mode")
    @Operation(summary = "Set default payment mode", description = "Update default payment mode for the authenticated user")
    public ResponseEntity<ApiResponse> updateDefaultPaymentMode(@PathVariable Long id) {
        try {
            AccountResponseDto updated = accountService.updateDefaultPaymentMode(id);
            return ResponseEntity.ok(new ApiResponse("Update success!", updated));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/all")
    @Operation(summary = "Get all accounts", description = "Retrieve all available accounts grouped by account type")
    public ResponseEntity<ApiResponse> getAllAccounts() {
        try {
            Map<AccountType, List<AccountResponseDto>> groupedAccounts = accountService.getAllAccountsByUserGroupedByType();
            return ResponseEntity.ok(new ApiResponse("Found!", groupedAccounts));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error: " + e.getMessage(), null));
        }
    }


    @PostMapping("/add")
    @Operation(summary = "Add a new account", description = "Create a new account for the authenticated user")
    public ResponseEntity<ApiResponse> addAccount(@RequestBody AccountRequestDto requestDto) {
        try {
            AccountResponseDto response = accountService.addAccount(requestDto);
            return ResponseEntity.status(CREATED).body(new ApiResponse("Success", response));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/account/{id}")
    @Operation(summary = "Get account by ID", description = "Retrieve an account using its ID")
    public ResponseEntity<ApiResponse> getAccountById(@PathVariable Long id) {
        try {
            AccountResponseDto response = accountService.getAccountById(id);
            return ResponseEntity.ok(new ApiResponse("Found", response));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/account/name/{name}")
    @Operation(summary = "Get account by name", description = "Retrieve an account using its name")
    public ResponseEntity<ApiResponse> getAccountByName(@PathVariable String name) {
        try {
            AccountResponseDto response = accountService.getAccountByName(name);
            return ResponseEntity.ok(new ApiResponse("Found", response));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/account/{id}")
    @Operation(summary = "Delete account", description = "Delete an account by its ID")
    public ResponseEntity<ApiResponse> deleteAccount(@PathVariable Long id) {
        try {
            accountService.deleteAccountById(id);
            return ResponseEntity.ok(new ApiResponse("Deleted successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/account/{id}")
    @Operation(summary = "Update account", description = "Update account information by ID")
    public ResponseEntity<ApiResponse> updateAccount(@PathVariable Long id, @RequestBody AccountRequestDto accountDto) {
        try {
            AccountResponseDto updated = accountService.updateAccount(accountDto, id);
            return ResponseEntity.ok(new ApiResponse("Update success!", updated));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
