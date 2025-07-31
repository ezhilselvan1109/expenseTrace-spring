package com.expensetrace.app.controller;

import com.expensetrace.app.exception.AlreadyExistsException;
import com.expensetrace.app.exception.ResourceNotFoundException;
import com.expensetrace.app.dto.request.BankRequestDto;
import com.expensetrace.app.dto.request.CreditCardRequestDto;
import com.expensetrace.app.dto.request.WalletRequestDto;
import com.expensetrace.app.response.ApiResponse;
import com.expensetrace.app.dto.response.AccountResponseDto;
import com.expensetrace.app.service.account.IAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

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

    @GetMapping("available-amount")
    @Operation(summary = "Get available-amount", description = "Retrieve the available-amount for the authenticated user")
    public ResponseEntity<ApiResponse> getAvailableAmount() {
        try {
            BigDecimal availableAmount = accountService.getAvailableAmount();
            return ResponseEntity.ok(new ApiResponse("Found!", availableAmount));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("credit/outstanding")
    @Operation(summary = "Get credit-amount", description = "Retrieve the available-amount for the authenticated user")
    public ResponseEntity<ApiResponse> getCreditOutstanding() {
        try {
            BigDecimal availableAmount = accountService.getCreditOutstanding();
            return ResponseEntity.ok(new ApiResponse("Found!", availableAmount));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("credit/available")
    @Operation(summary = "Get credit-amount", description = "Retrieve the available-amount for the authenticated user")
    public ResponseEntity<ApiResponse> getCreditAvailable() {
        try {
            BigDecimal availableAmount = accountService.getCreditAvailable();
            return ResponseEntity.ok(new ApiResponse("Found!", availableAmount));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{id}/default-payment-mode")
    @Operation(summary = "Set default payment mode", description = "Update default payment mode for the authenticated user")
    public ResponseEntity<ApiResponse> updateDefaultPaymentMode(@PathVariable UUID id) {
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
            List<AccountResponseDto> groupedAccounts = accountService.getAllAccountsByUserGroupedByType();
            return ResponseEntity.ok(new ApiResponse("Found!", groupedAccounts));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error: " + e.getMessage(), null));
        }
    }

    @GetMapping("/account/{id}")
    @Operation(summary = "Get account by ID", description = "Retrieve an account using its ID")
    public ResponseEntity<ApiResponse> getAccountById(@PathVariable UUID id) {
        try {
            AccountResponseDto response = accountService.getAccountById(id);
            return ResponseEntity.ok(new ApiResponse("Found", response));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/bank")
    @Operation(summary = "Get bank account", description = "Retrieve an all bank account")
    public ResponseEntity<ApiResponse> getAllBankAccountsByUser() {
        try {
            List<AccountResponseDto> response = accountService.getAllBankAccountsByUser();
            return ResponseEntity.ok(new ApiResponse("Found", response));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/wallet")
    @Operation(summary = "Get wallet account", description = "Retrieve an all wallet account")
    public ResponseEntity<ApiResponse> getAllWalletAccountsByUser() {
        try {
            List<AccountResponseDto> response = accountService.getAllWalletAccountsByUser();
            return ResponseEntity.ok(new ApiResponse("Found", response));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/credit-card")
    @Operation(summary = "Get all credit card account", description = "Retrieve an all credit card account")
    public ResponseEntity<ApiResponse> getAllCreditCardAccountsByUser() {
        try {
            List<AccountResponseDto> response = accountService.getAllCreditCardAccountsByUser();
            return ResponseEntity.ok(new ApiResponse("Found", response));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/cash")
    @Operation(summary = "Get all cash in account", description = "Retrieve an all cash account")
    public ResponseEntity<ApiResponse> getAllCashAccountsByUser() {
        try {
            List<AccountResponseDto> response = accountService.getAllCashAccountsByUser();
            return ResponseEntity.ok(new ApiResponse("Found", response));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/bank")
    @Operation(summary = "Add a new bank account", description = "Create a new bank account for the authenticated user")
    public ResponseEntity<ApiResponse> addBankAccount(@Valid @RequestBody BankRequestDto requestDto) {
        try {
            AccountResponseDto response = accountService.addBankAccount(requestDto);
            return ResponseEntity.status(CREATED).body(new ApiResponse("Success", response));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/wallet")
    @Operation(summary = "Add a new wallet account", description = "Create a new wallet account for the authenticated user")
    public ResponseEntity<ApiResponse> addWallet(@Valid @RequestBody WalletRequestDto requestDto) {
        try {
            AccountResponseDto response = accountService.addWalletAccount(requestDto);
            return ResponseEntity.status(CREATED).body(new ApiResponse("Success", response));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/debit-card")
    @Operation(summary = "Add a new DebitCard account", description = "Create a new DebitCard account for the authenticated user")
    public ResponseEntity<ApiResponse> addDebitCard(@Valid @RequestBody CreditCardRequestDto requestDto) {
        try {
            AccountResponseDto response = accountService.addCreditCardAccount(requestDto);
            return ResponseEntity.status(CREATED).body(new ApiResponse("Success", response));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }



    @DeleteMapping("/account/{id}")
    @Operation(summary = "Delete account", description = "Delete an account by its ID")
    public ResponseEntity<ApiResponse> deleteAccount(@PathVariable UUID id) {
        try {
            accountService.deleteAccountById(id);
            return ResponseEntity.ok(new ApiResponse("Deleted successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/bank/{id}")
    @Operation(summary = "Update bank account", description = "Update bank account information by ID")
    public ResponseEntity<ApiResponse> updateBankAccount(@PathVariable UUID id, @Valid @RequestBody BankRequestDto accountDto) {
        try {
            AccountResponseDto updated = accountService.updateBankAccount(accountDto, id);
            return ResponseEntity.ok(new ApiResponse("Update success!", updated));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/wallet/{id}")
    @Operation(summary = "Update account", description = "Update account information by ID")
    public ResponseEntity<ApiResponse> updateWalletAccount(@PathVariable UUID id, @Valid @RequestBody WalletRequestDto accountDto) {
        try {
            AccountResponseDto updated = accountService.updateWalletAccount(accountDto, id);
            return ResponseEntity.ok(new ApiResponse("Update success!", updated));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/credit-card/{id}")
    @Operation(summary = "Update account", description = "Update account information by ID")
    public ResponseEntity<ApiResponse> updateCreditCardAccount(@PathVariable UUID id, @Valid @RequestBody CreditCardRequestDto accountDto) {
        try {
            AccountResponseDto updated = accountService.updateCreditCardAccount(accountDto, id);
            return ResponseEntity.ok(new ApiResponse("Update success!", updated));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
