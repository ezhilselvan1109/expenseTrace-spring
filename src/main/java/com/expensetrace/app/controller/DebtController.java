package com.expensetrace.app.controller;

import com.expensetrace.app.dto.request.DebtRequestDto;
import com.expensetrace.app.response.ApiResponse;
import com.expensetrace.app.dto.response.DebtResponseDto;
import com.expensetrace.app.dto.response.DebtSummaryResponseDto;
import com.expensetrace.app.service.debt.IDebtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/debts")
@Tag(name = "Debt", description = "manage debts")
public class DebtController {
    private final IDebtService debtService;
    @PostMapping
    @Operation(summary = "Add a new debt", description = "Create a new debt for the authenticated user")
    public ResponseEntity<ApiResponse> addDebt(@Valid @RequestBody DebtRequestDto debtRequestDto) {
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
    public ResponseEntity<ApiResponse> updateDebt(@PathVariable UUID id, @Valid @RequestBody DebtRequestDto debtRequestDto) {
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

    @GetMapping("all")
    @Operation(summary = "get all debt", description = "get all debt for the authenticated user")
    public ResponseEntity<ApiResponse> getAllDebt() {
        List<DebtResponseDto> debts = debtService.getAllDebtsByUser();
        return ResponseEntity.ok(new ApiResponse("Fetched debts", debts));
    }

    @GetMapping("/borrowing")
    @Operation(summary = "Get all Borrowing debts with pagination")
    public ResponseEntity<ApiResponse> getAllBorrowingDebt(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<DebtResponseDto> txns = debtService.getAllBorrowingDebtsByUser(page, size);
        return ResponseEntity.ok(new ApiResponse("Fetched transactions", txns));
    }

    @GetMapping("/lending")
    @Operation(summary = "Get all Lending debts with pagination")
    public ResponseEntity<ApiResponse> getAllLendingDebt(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<DebtResponseDto> txns = debtService.getAllLendingDebtsByUser(page, size);
        return ResponseEntity.ok(new ApiResponse("Fetched transactions", txns));
    }

    @GetMapping
    @Operation(summary = "Get all debts with pagination")
    public ResponseEntity<ApiResponse> getAllDebt(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<DebtResponseDto> txns = debtService.getAllDebtsByUser(page, size);
        return ResponseEntity.ok(new ApiResponse("Fetched transactions", txns));
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

    @GetMapping("/payable")
    @Operation(summary = "Get total Payable amount", description = "Sum of all borrowing debts")
    public ResponseEntity<ApiResponse> getPayable() {
        BigDecimal totalPayable = debtService.getTotalPayableAmount();
        return ResponseEntity.ok(new ApiResponse("Total Payable fetched", totalPayable));
    }

    @GetMapping("/receivable")
    @Operation(summary = "Get total Receivable amount", description = "Sum of all lending debts")
    public ResponseEntity<ApiResponse> getReceivable() {
        BigDecimal totalReceivable = debtService.getTotalReceivableAmount();
        return ResponseEntity.ok(new ApiResponse("Total Receivable fetched", totalReceivable));
    }

    @GetMapping("/summary")
    @Operation(summary = "Get total Payable and Receivable summary")
    public ResponseEntity<ApiResponse> getDebtSummary() {
        DebtSummaryResponseDto summary = debtService.getPayableAndReceivableSummary();
        return ResponseEntity.ok(new ApiResponse("Debt summary fetched", summary));
    }

}
