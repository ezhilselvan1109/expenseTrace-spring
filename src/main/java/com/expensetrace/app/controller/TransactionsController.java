package com.expensetrace.app.controller;


import com.expensetrace.app.requestDto.TransactionRequestDTO;
import com.expensetrace.app.response.ApiResponse;
import com.expensetrace.app.responseDto.TransactionResponseDTO;
import com.expensetrace.app.service.transaction.ITransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/transactions")
@Tag(name = "Transactions", description = "Endpoints for managing transactions")
public class TransactionsController {

    private final ITransactionService transactionService;

    @PostMapping
    @Operation(summary = "Create a transaction")
    public ResponseEntity<ApiResponse> createTransaction(@RequestBody TransactionRequestDTO dto) {
        try {
            TransactionResponseDTO txn = transactionService.createTransaction(dto);
            return ResponseEntity.status(CREATED).body(new ApiResponse("Transaction created", txn));
        } catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping
    @Operation(summary = "Get all transactions")
    public ResponseEntity<ApiResponse> getAllTransactions() {
        List<TransactionResponseDTO> txns = transactionService.getAllTransactionsByUser();
        return ResponseEntity.ok(new ApiResponse("Fetched transactions", txns));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get transaction by ID")
    public ResponseEntity<ApiResponse> getTransaction(@PathVariable Long id) {
        try {
            TransactionResponseDTO txn = transactionService.getTransactionById(id);
            return ResponseEntity.ok(new ApiResponse("Transaction found", txn));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete transaction")
    public ResponseEntity<ApiResponse> deleteTransaction(@PathVariable Long id) {
        try {
            transactionService.deleteTransactionById(id);
            return ResponseEntity.ok(new ApiResponse("Transaction deleted", null));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update transaction")
    public ResponseEntity<ApiResponse> updateTransaction(@PathVariable Long id, @RequestBody TransactionRequestDTO dto) {
        try {
            TransactionResponseDTO updated = transactionService.updateTransaction(id, dto);
            return ResponseEntity.ok(new ApiResponse("Transaction updated", updated));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
