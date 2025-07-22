package com.expensetrace.app.controller;


import com.expensetrace.app.requestDto.TransactionRequestDto;
import com.expensetrace.app.response.ApiResponse;
import com.expensetrace.app.responseDto.TransactionResponseDto;
import com.expensetrace.app.service.transaction.ITransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/transactions")
@Tag(name = "Transactions", description = "Endpoints for managing transactions")
public class TransactionsController {

    private final ITransactionService transactionService;

    @PostMapping
    @Operation(summary = "Create a transaction")
    public ResponseEntity<ApiResponse> createTransaction(@RequestBody TransactionRequestDto dto) {
        try {
            TransactionResponseDto txn = transactionService.createTransaction(dto);
            return ResponseEntity.status(CREATED).body(new ApiResponse("Transaction created", txn));
        } catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/all")
    @Operation(summary = "Get all transactions")
    public ResponseEntity<ApiResponse> getAllTransactions() {
        List<TransactionResponseDto> txns = transactionService.getAllTransactionsByUser();
        return ResponseEntity.ok(new ApiResponse("Fetched transactions", txns));
    }

    @GetMapping
    @Operation(summary = "Get all transactions with pagination")
    public ResponseEntity<ApiResponse> getAllTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<TransactionResponseDto> txns = transactionService.getAllTransactionsByUser(page, size);
        return ResponseEntity.ok(new ApiResponse("Fetched transactions", txns));
    }


    @GetMapping("/{id}")
    @Operation(summary = "Get transaction by ID")
    public ResponseEntity<ApiResponse> getTransaction(@PathVariable UUID id) {
        try {
            TransactionResponseDto txn = transactionService.getTransactionById(id);
            return ResponseEntity.ok(new ApiResponse("Transaction found", txn));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/account/{id}")
    @Operation(summary = "Get all transaction by account ID")
    public ResponseEntity<ApiResponse> getAllTransactionByAccountId(@PathVariable UUID accountId) {
        try {
            TransactionResponseDto txn = transactionService.getTransactionById(accountId);
            return ResponseEntity.ok(new ApiResponse("Transaction found", txn));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
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

    @PutMapping("/{id}")
    @Operation(summary = "Update transaction")
    public ResponseEntity<ApiResponse> updateTransaction(@PathVariable UUID id, @RequestBody TransactionRequestDto dto) {
        try {
            TransactionResponseDto updated = transactionService.updateTransaction(id, dto);
            return ResponseEntity.ok(new ApiResponse("Transaction updated", updated));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
