package com.expensetrace.app.controller;

import com.expensetrace.app.requestDto.DebtTransactionRequestDto;
import com.expensetrace.app.response.ApiResponse;
import com.expensetrace.app.responseDto.DebtTransactionResponseDto;
import com.expensetrace.app.service.debtTransaction.IDebtTransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/debts-transactions")
@Tag(name = "Debt Transactions", description = "manage debts Transactions")
public class DebtTransactionsController {
    private final IDebtTransactionService debtTransactionService;
    @PostMapping("/{debtId}")
    @Operation(summary = "Add a new debt Transaction", description = "Create a new debt Transaction for the authenticated user")
    public ResponseEntity<ApiResponse> addDebtTransaction(@PathVariable UUID debtId, @RequestBody DebtTransactionRequestDto debtTransactionRequestDto) {
        try {
            DebtTransactionResponseDto debt = debtTransactionService.createDebtTransaction(debtId,debtTransactionRequestDto);
            return ResponseEntity.status(CREATED).body(new ApiResponse("Debt created", debt));
        } catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "update debt Transaction", description = "update debt Transaction for the authenticated user")
    public ResponseEntity<ApiResponse> updateDebtTransaction(@PathVariable UUID id, @RequestBody DebtTransactionRequestDto debtTransactionRequestDto) {
        try {
            DebtTransactionResponseDto updated = debtTransactionService.updateDebtTransaction(id, debtTransactionRequestDto);
            return ResponseEntity.ok(new ApiResponse("Debt updated", updated));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "get debt Transaction", description = "get debt Transaction by id for the authenticated user")
    public ResponseEntity<ApiResponse> getDebtTransaction(@PathVariable UUID id) {
        try {
            DebtTransactionResponseDto debtTransaction = debtTransactionService.getDebtTransactionById(id);
            return ResponseEntity.ok(new ApiResponse("Debt found", debtTransaction));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{debtId}")
    @Operation(summary = "get all debt Transaction", description = "get all debt Transaction for the authenticated user")
    public ResponseEntity<ApiResponse> getAllDebtTransaction(@PathVariable UUID debtId) {
        List<DebtTransactionResponseDto> debts = debtTransactionService.getAllDebtTransactionsByUser(debtId);
        return ResponseEntity.ok(new ApiResponse("Fetched debts", debts));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete debt Transaction", description = "delete debt Transaction for the authenticated user")
    public ResponseEntity<ApiResponse> deleteDebtTransaction(@PathVariable UUID id) {
        try {
            debtTransactionService.deleteDebtTransactionById(id);
            return ResponseEntity.ok(new ApiResponse("Debt deleted", null));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
