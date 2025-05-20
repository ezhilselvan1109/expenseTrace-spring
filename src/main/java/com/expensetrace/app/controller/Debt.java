package com.expensetrace.app.controller;

import com.expensetrace.app.requestDto.DebtRequestDto;
import com.expensetrace.app.response.ApiResponse;
import com.expensetrace.app.responseDto.DebtResponseDto;
import com.expensetrace.app.service.debt.IDebtService;
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
@RequestMapping("${api.prefix}/debts")
@Tag(name = "Debt", description = "manage debts")
public class Debt {
    private final IDebtService debtService;
    @PostMapping
    @Operation(summary = "Add a new debt", description = "Create a new debt for the authenticated user")
    public ResponseEntity<ApiResponse> addDebt(@RequestBody DebtRequestDto debtRequestDto) {
        System.out.println("debtRequestDto : "+debtRequestDto);
        try {
            DebtResponseDto debt = debtService.createDebt(debtRequestDto);
            return ResponseEntity.status(CREATED).body(new ApiResponse("Debt created", debt));
        } catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "update debt", description = "update debt for the authenticated user")
    public ResponseEntity<ApiResponse> updateDebt(@PathVariable UUID id, @RequestBody DebtRequestDto debtRequestDto) {
        try {
            DebtResponseDto updated = debtService.updateDebt(id, debtRequestDto);
            return ResponseEntity.ok(new ApiResponse("Debt updated", updated));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "get debt", description = "get debt by id for the authenticated user")
    public ResponseEntity<ApiResponse> getDebt(@PathVariable UUID id) {
        try {
            DebtResponseDto debt = debtService.getDebtById(id);
            return ResponseEntity.ok(new ApiResponse("Debt found", debt));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping
    @Operation(summary = "get all debt", description = "get all debt for the authenticated user")
    public ResponseEntity<ApiResponse> getAllDebt() {
        List<DebtResponseDto> debts = debtService.getAllDebtsByUser();
        return ResponseEntity.ok(new ApiResponse("Fetched debts", debts));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete debt", description = "delete debt for the authenticated user")
    public ResponseEntity<ApiResponse> deleteDebt(@PathVariable UUID id) {
        try {
            debtService.deleteDebtById(id);
            return ResponseEntity.ok(new ApiResponse("Debt deleted", null));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
