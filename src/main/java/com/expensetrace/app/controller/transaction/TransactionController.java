package com.expensetrace.app.controller.transaction;

import com.expensetrace.app.dto.request.transaction.*;
import com.expensetrace.app.dto.request.transaction.record.DebtAdjustmentRequestDto;
import com.expensetrace.app.dto.request.transaction.record.DebtPaidRequestDto;
import com.expensetrace.app.dto.request.transaction.record.DebtReceivedRequestDto;
import com.expensetrace.app.dto.response.transaction.*;
import com.expensetrace.app.dto.response.transaction.record.DebtAdjustmentResponseDto;
import com.expensetrace.app.dto.response.transaction.record.DebtPaidResponseDto;
import com.expensetrace.app.dto.response.transaction.record.DebtReceivedResponseDto;
import com.expensetrace.app.enums.TransactionSummaryType;
import com.expensetrace.app.response.ApiResponse;
import com.expensetrace.app.service.transaction.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionServiceFactory factory;
    private final CommonTransactionService commonService;
    private final TransactionAggregationService aggregationService;
    private final TransactionSummaryService summaryService;
    private final DebtRecordSummaryService debtRecordSummaryService;

    // -------- Income --------
    @PostMapping("/income")
    public ResponseEntity<ApiResponse> createIncome(@RequestBody IncomeTransactionRequestDto dto) {
        return ResponseEntity.ok(
                new ApiResponse("Created!",factory.<IncomeTransactionRequestDto, IncomeTransactionResponseDto>
                getService("incomeTransactionService").create(dto)));
    }

    @PutMapping("/income/{id}")
    public ResponseEntity<ApiResponse> updateIncome(@PathVariable UUID id, @RequestBody IncomeTransactionRequestDto dto) {
        return ResponseEntity.ok(new ApiResponse("Updated!",factory.<IncomeTransactionRequestDto, IncomeTransactionResponseDto>
                getService("incomeTransactionService").update(id, dto)));
    }
    // -------- Expense --------
    @PostMapping("/expense")
    public ResponseEntity<ApiResponse> createExpense(@Valid @RequestBody ExpenseTransactionRequestDto dto) {
        return ResponseEntity.ok(new ApiResponse("Created!",factory.<ExpenseTransactionRequestDto, ExpenseTransactionResponseDto>
                getService("expenseTransactionService").create(dto)));
    }

    @PutMapping("/expense/{id}")
    public ResponseEntity<ApiResponse> updateExpense(@PathVariable UUID id, @RequestBody ExpenseTransactionRequestDto dto) {
        return ResponseEntity.ok(new ApiResponse("Updated!",factory.<ExpenseTransactionRequestDto, ExpenseTransactionResponseDto>
                getService("expenseTransactionService").update(id, dto)));
    }

    // -------- Transfer --------
    @PostMapping("/transfer")
    public ResponseEntity<ApiResponse> createTransfer(@RequestBody TransferTransactionRequestDto dto) {
        return ResponseEntity.ok(new ApiResponse("Created!",factory.<TransferTransactionRequestDto, TransferTransactionResponseDto>
                getService("transferTransactionService").create(dto)));
    }

    @PutMapping("/transfer/{id}")
    public ResponseEntity<ApiResponse> updateTransfer(@PathVariable UUID id, @RequestBody TransferTransactionRequestDto dto) {
        return ResponseEntity.ok(new ApiResponse("Updated!",factory.<TransferTransactionRequestDto, TransferTransactionResponseDto>
                getService("transferTransactionService").update(id, dto)));
    }

    // -------- Adjustment --------
    @PostMapping("/adjustment")
    public ResponseEntity<ApiResponse> createAdjustment(@RequestBody AdjustmentTransactionRequestDto dto) {
        return ResponseEntity.ok(new ApiResponse("Created!",factory.<AdjustmentTransactionRequestDto, AdjustmentTransactionResponseDto>
                getService("adjustmentTransactionService").create(dto)));
    }

    @PutMapping("/adjustment/{id}")
    public ResponseEntity<ApiResponse> updateAdjustment(@PathVariable UUID id, @RequestBody AdjustmentTransactionRequestDto dto) {
        return ResponseEntity.ok(new ApiResponse("Updated!",factory.<AdjustmentTransactionRequestDto, AdjustmentTransactionResponseDto>
                getService("adjustmentTransactionService").update(id, dto)));
    }

    // -------- Debt → Paid, Received, Adjustment --------
    @PostMapping("/debt/{debtId}/paid")
    public ResponseEntity<ApiResponse> createDebtPaid(
            @PathVariable UUID debtId,
            @RequestBody DebtPaidRequestDto dto) {
        return ResponseEntity.ok(new ApiResponse("Created!",factory.<DebtPaidRequestDto, DebtPaidResponseDto>
                getDebtService("debtPaidService").create(dto, debtId)));
    }

    @PutMapping("/debt/paid/{id}")
    public ResponseEntity<ApiResponse> updateDebtPaid(@PathVariable UUID id, @RequestBody DebtPaidRequestDto dto) {
        return ResponseEntity.ok(new ApiResponse("Updated!",factory.<DebtPaidRequestDto, DebtPaidResponseDto>
                getService("debtPaidService").update(id, dto)));
    }

    @PostMapping("/debt/{debtId}/adjustment")
    public ResponseEntity<ApiResponse> createDebtAdjustment(
            @PathVariable UUID debtId,
            @RequestBody DebtAdjustmentRequestDto dto) {
        return ResponseEntity.ok(new ApiResponse("Created!",factory.<DebtAdjustmentRequestDto, DebtAdjustmentResponseDto>
                getDebtService("adjustmentRecordService").create(dto, debtId)));
    }

    @PutMapping("/debt/adjustment/{id}")
    public ResponseEntity<ApiResponse> updateDebtAdjustment(@PathVariable UUID id, @RequestBody DebtAdjustmentRequestDto dto) {
        return ResponseEntity.ok(new ApiResponse("Created!",factory.<DebtAdjustmentRequestDto, DebtAdjustmentResponseDto>
                getService("adjustmentRecordService").update(id, dto)));
    }

    @PostMapping("/debt/{debtId}/received")
    public ResponseEntity<ApiResponse> createDebtReceived(
            @PathVariable UUID debtId,
            @RequestBody DebtReceivedRequestDto dto) {
        return ResponseEntity.ok(new ApiResponse("Created!",factory.<DebtReceivedRequestDto, DebtReceivedResponseDto>
                getDebtService("receivedRecordService").create(dto, debtId)));
    }

    @PutMapping("/debt/received/{id}")
    public ResponseEntity<ApiResponse> updateDebtReceived(@PathVariable UUID id, @RequestBody DebtReceivedRequestDto dto) {
        return ResponseEntity.ok(new ApiResponse("Created!",factory.<DebtReceivedRequestDto, DebtReceivedResponseDto>
                getService("receivedRecordService").update(id, dto)));
    }

    // -------- Common Delete --------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        commonService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getTransactionById(@PathVariable UUID id) {
        return ResponseEntity.ok(new ApiResponse("Found!",commonService.get(id)));
    }

    // 1. All standard transactions (Income, Expense, Transfer)
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllStandardTransactions(
            @PageableDefault(size = 10, sort = "txnAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(new ApiResponse("Found!",aggregationService.getAllStandardTransactions(pageable)));
    }

    // 2. All debt transactions (Paid, Received, Adjustment)
    @GetMapping("/debt/{debtId}/all")
    public ResponseEntity<ApiResponse> getAllDebtTransactions(@PathVariable UUID debtId,
            @PageableDefault(size = 10, sort = "txnAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(new ApiResponse("Found!",aggregationService.getAllDebtTransactions(debtId,pageable)));
    }

    @GetMapping("/debt/{debtId}/paid")
    public ResponseEntity<ApiResponse> getAllDebtPaidTransactions(@PathVariable UUID debtId,
            @PageableDefault(size = 10, sort = "txnAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(new ApiResponse("Found!",aggregationService.getAllDebtPaidTransactions(debtId,pageable)));
    }

    @GetMapping("/debt/{debtId}/received")
    public ResponseEntity<ApiResponse> getAllDebtReceivedTransactions(@PathVariable UUID debtId,
            @PageableDefault(size = 10, sort = "txnAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(new ApiResponse("Found!",aggregationService.getAllDebtReceivedTransactions(debtId,pageable)));
    }

    @GetMapping("/debt/{debtId}/adjustment")
    public ResponseEntity<ApiResponse> getAllDebtAdjustmentTransactions(@PathVariable UUID debtId,
            @PageableDefault(size = 10, sort = "txnAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(new ApiResponse("Found!",aggregationService.getAllDebtAdjustmentTransactions(debtId,pageable)));
    }

    // 3. All transactions by account (Income, Expense, Transfer, Adjustment, DebtPaid, DebtReceived)
    @GetMapping("/account/{accountId}/all")
    public ResponseEntity<ApiResponse> getAllByAccount(@PathVariable UUID accountId,
                                                       @PageableDefault(size = 10, sort = "txnAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(new ApiResponse("Found!",aggregationService.getAllTransactionsByAccount(accountId,pageable)));
    }

    @GetMapping("/account/{accountId}/debit")
    public ResponseEntity<ApiResponse> getAllDebitByAccount(@PathVariable UUID accountId,
                                                       @PageableDefault(size = 10, sort = "txnAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(new ApiResponse("Found!",aggregationService.getAllDebitTransactionsByAccount(accountId,pageable)));
    }

    @GetMapping("/account/{accountId}/credit")
    public ResponseEntity<ApiResponse> getAllCreditByAccount(@PathVariable UUID accountId,
                                                       @PageableDefault(size = 10, sort = "txnAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(new ApiResponse("Found!",aggregationService.getAllCreditTransactionsByAccount(accountId,pageable)));
    }

    @GetMapping("/account/{accountId}/adjustment")
    public ResponseEntity<ApiResponse> getAllAdjustmentByAccount(@PathVariable UUID accountId,
                                                       @PageableDefault(size = 10, sort = "txnAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(new ApiResponse("Found!",aggregationService.getAllAdjustmentTransactionsByAccount(accountId,pageable)));
    }

    @GetMapping("/summary")
    public ResponseEntity<ApiResponse> getSummary(
            @RequestParam(defaultValue = "2") int rangeCode) {
        TransactionSummaryType rangeType = TransactionSummaryType.fromCode(rangeCode);
        TransactionSummaryDto dto = summaryService.getSummary(rangeType);
        return ResponseEntity.ok(new ApiResponse("Found!", dto));
    }


    @GetMapping("/recent")
    public ResponseEntity<ApiResponse> getRecentTransactions() {
        return ResponseEntity.ok(
                new ApiResponse("Found!", aggregationService.getRecentTransactions(6))
        );
    }

    @GetMapping("/by-date")
    public ResponseEntity<ApiResponse> getTransactionsByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @PageableDefault(size = 10) Pageable pageable) {

        TransactionDateSummaryDto summary = aggregationService.getTransactionsByDate(date, pageable);
        return ResponseEntity.ok(new ApiResponse("Found!", summary));
    }

    @GetMapping("/{debtId}/summary")
    public ResponseEntity<ApiResponse> getDebtRecordSummary(@PathVariable UUID debtId) {
        return ResponseEntity.ok(
                new ApiResponse("Found!", debtRecordSummaryService.getSummary(debtId))
        );
    }
}

