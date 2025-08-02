package com.expensetrace.app.controller;


import com.expensetrace.app.dto.request.transaction.*;
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
            return ResponseEntity.status(BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
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
}
