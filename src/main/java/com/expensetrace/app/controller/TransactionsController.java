package com.expensetrace.app.controller;


import com.expensetrace.app.dto.request.transaction.*;
import com.expensetrace.app.enums.AccountType;
import com.expensetrace.app.enums.TransactionType;
import com.expensetrace.app.exception.ResourceNotFoundException;
import com.expensetrace.app.exception.ValidationException;
import com.expensetrace.app.response.ApiResponse;
import com.expensetrace.app.dto.response.transaction.TransactionResponseDto;
import com.expensetrace.app.service.transaction.ITransactionService;
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
@RequestMapping("${api.prefix}/transactions")
@Tag(name = "Transactions", description = "Endpoints for managing transactions")
public class TransactionsController {

    private final ITransactionService transactionService;

    @PostMapping("/expense")
    @Operation(summary = "Create expense a transaction")
    public ResponseEntity<ApiResponse> createExpenseTransaction(@Valid @RequestBody ExpenseTransactionRequestDto dto) {
        try {
            TransactionResponseDto txn = transactionService.createTransaction(dto);
            return ResponseEntity.status(CREATED).body(new ApiResponse("Transaction created", txn));
        } catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/income")
    @Operation(summary = "Create income a transaction")
    public ResponseEntity<ApiResponse> createIncomeTransaction(@Valid @RequestBody IncomeTransactionRequestDto dto) {
        try {
            TransactionResponseDto txn = transactionService.createTransaction(dto);
            return ResponseEntity.status(CREATED).body(new ApiResponse("Transaction created", txn));
        } catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/transfer")
    @Operation(summary = "Create transfer a transaction")
    public ResponseEntity<ApiResponse> createTransferTransaction(@Valid @RequestBody TransferTransactionRequestDto dto) {
        try {
            System.out.println("Bachelorhood..............");
            TransactionResponseDto txn = transactionService.createTransaction(dto);
            return ResponseEntity.status(CREATED).body(new ApiResponse("Transaction created", txn));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/adjustment")
    @Operation(summary = "Create adjustment a transaction")
    public ResponseEntity<ApiResponse> createAdjustmentTransaction(@Valid @RequestBody AdjustmentTransactionRequestDto dto) {
        try {
            TransactionResponseDto txn = transactionService.createTransaction(dto);
            return ResponseEntity.status(CREATED).body(new ApiResponse("Transaction created", txn));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Unexpected error", null));
        }
    }
    @PutMapping("/expense/{id}")
    @Operation(summary = "Update an expense transaction")
    public ResponseEntity<ApiResponse> updateExpenseTransaction(
            @PathVariable UUID id,
            @Valid @RequestBody ExpenseTransactionRequestDto dto) {
        try {
            TransactionResponseDto updatedTxn = transactionService.updateTransaction(id, dto);
            return ResponseEntity.ok(new ApiResponse("Expense transaction updated", updatedTxn));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Unexpected error", null));
        }
    }

    @PutMapping("/income/{id}")
    @Operation(summary = "Update an income transaction")
    public ResponseEntity<ApiResponse> updateIncomeTransaction(
            @PathVariable UUID id,
            @Valid @RequestBody IncomeTransactionRequestDto dto) {
        try {
            TransactionResponseDto updatedTxn = transactionService.updateTransaction(id, dto);
            return ResponseEntity.ok(new ApiResponse("Income transaction updated", updatedTxn));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Unexpected error", null));
        }
    }

    @PutMapping("/transfer/{id}")
    @Operation(summary = "Update a transfer transaction")
    public ResponseEntity<ApiResponse> updateTransferTransaction(
            @PathVariable UUID id,
            @Valid @RequestBody TransferTransactionRequestDto dto) {
        try {
            TransactionResponseDto updatedTxn = transactionService.updateTransaction(id, dto);
            return ResponseEntity.ok(new ApiResponse("Transfer transaction updated", updatedTxn));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Unexpected error", null));
        }
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Delete transaction")
    public ResponseEntity<ApiResponse> deleteTransaction(@PathVariable UUID id) {
        try {
            transactionService.deleteTransactionById(id);
            return ResponseEntity.ok(new ApiResponse("Transaction deleted", null));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get transaction details by ID and type")
    public ResponseEntity<ApiResponse> getTransactionById(
            @PathVariable UUID id) {
        try {
            TransactionResponseDto dto = transactionService.getTransactionByIdAndType(id);
            return ResponseEntity.ok(new ApiResponse("Transaction fetched", dto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Unexpected error"+e.getMessage(), null));
        }
    }

    @GetMapping
    @Operation(summary = "Get all transactions for logged-in user (paginated)")
    public ResponseEntity<ApiResponse> getAllTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            var transactions = transactionService.getAllTransactions(page, size);
            return ResponseEntity.ok(new ApiResponse("Transactions fetched", transactions));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Unexpected error: " + e.getMessage(), null));
        }
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Get all transactions for logged-in user (paginated)")
    public ResponseEntity<ApiResponse> getAllTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,@PathVariable Integer type) {
        try {
            var transactions = transactionService.getAllTransactions(page, size, TransactionType.fromCode(type));
            return ResponseEntity.ok(new ApiResponse("Transactions fetched", transactions));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Unexpected error: " + e.getMessage(), null));
        }
    }
}
