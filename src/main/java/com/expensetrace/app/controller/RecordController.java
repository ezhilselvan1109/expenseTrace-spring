package com.expensetrace.app.controller;

import com.expensetrace.app.dto.request.transaction.record.AdjustmentRequestDto;
import com.expensetrace.app.dto.request.transaction.record.PaidRequestDto;
import com.expensetrace.app.dto.request.transaction.record.ReceivedRequestDto;
import com.expensetrace.app.dto.response.transaction.TransactionResponseDto;
import com.expensetrace.app.response.ApiResponse;
import com.expensetrace.app.service.transaction.ITransactionService;
import com.expensetrace.app.service.transaction.record.adjustment.IAdjustmentService;
import com.expensetrace.app.service.transaction.record.paid.IPaidService;
import com.expensetrace.app.service.transaction.record.received.IReceivedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/records")
@Tag(name = " Records", description = "manage records")
public class RecordController {
    private final ITransactionService transactionService;
    private final IPaidService paidService;
    private final IReceivedService receivedService;
    private final IAdjustmentService adjustmentService;

    @PostMapping("/paid")
    @Operation(summary = "Add a new debt paid Record", description = "Create a new debt paid Record for the authenticated user")
    public ResponseEntity<ApiResponse> addPaidRecord(@Valid @RequestBody PaidRequestDto paidRequestDto) {
        try {
            TransactionResponseDto tnx = transactionService.createTransaction(paidRequestDto);
            return ResponseEntity.status(CREATED).body(new ApiResponse(" created", tnx));
        } catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/received")
    @Operation(summary = "Add a new debt received Record", description = "Create a new debt received Record for the authenticated user")
    public ResponseEntity<ApiResponse> addReceivedRecord(@Valid @RequestBody ReceivedRequestDto receivedRequestDto) {
        try {
            TransactionResponseDto tnx = transactionService.createTransaction(receivedRequestDto);
            return ResponseEntity.status(CREATED).body(new ApiResponse(" created", tnx));
        } catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/adjustment")
    @Operation(summary = "Add a new debt adjustment Record", description = "Create a new debt adjustment Record for the authenticated user")
    public ResponseEntity<ApiResponse> addAdjustmentRecord(@Valid @RequestBody AdjustmentRequestDto adjustmentRequestDto) {
        try {
            TransactionResponseDto tnx = transactionService.createTransaction(adjustmentRequestDto);
            return ResponseEntity.status(CREATED).body(new ApiResponse(" created", tnx));
        } catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{id}/adjustment")
    @Operation(summary = "update debt adjustment Record", description = "update debt adjustment Record for the authenticated user")
    public ResponseEntity<ApiResponse> updateAdjustmentRecord(@PathVariable UUID id, @Valid @RequestBody AdjustmentRequestDto adjustmentRequestDto) {
        try {
            TransactionResponseDto tnx = transactionService.updateTransaction(id,adjustmentRequestDto);
            return ResponseEntity.status(CREATED).body(new ApiResponse(" created", tnx));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{id}/received")
    @Operation(summary = "update debt received Record", description = "update debt received Record for the authenticated user")
    public ResponseEntity<ApiResponse> updateReceivedRecord(@PathVariable UUID id, @Valid @RequestBody ReceivedRequestDto receivedRequestDto) {
        try {
            TransactionResponseDto tnx = transactionService.updateTransaction(id,receivedRequestDto);
            return ResponseEntity.status(CREATED).body(new ApiResponse(" created", tnx));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{id}/paid")
    @Operation(summary = "update debt paid Record", description = "update debt paid Record for the authenticated user")
    public ResponseEntity<ApiResponse> updatePaidRecord(@PathVariable UUID id, @Valid @RequestBody PaidRequestDto paidRequestDto) {
        try {
            TransactionResponseDto tnx = transactionService.updateTransaction(id,paidRequestDto);
            return ResponseEntity.status(CREATED).body(new ApiResponse(" created", tnx));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "get debt Record", description = "get debt Record by id for the authenticated user")
    public ResponseEntity<ApiResponse> getRecord(@PathVariable UUID id) {
        try {
            TransactionResponseDto tnx = transactionService.getTransactionById(id);
            return ResponseEntity.status(CREATED).body(new ApiResponse(" created", tnx));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{debtId}/all")
    @Operation(summary = "get all debt Record with pagination", description = "get all debt Record with pagination for the authenticated user")
    public ResponseEntity<ApiResponse> getAllRecords(@PathVariable UUID debtId, @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int size) {
        Page<TransactionResponseDto> debts = transactionService.getAllRecords(debtId, page, size);
        return ResponseEntity.ok(new ApiResponse("Fetched debts", debts));
    }

    @GetMapping("/{debtId}/paid")
    @Operation(summary = "get all paid debt Record", description = "get all paid debt Record for the authenticated user")
    public ResponseEntity<ApiResponse> getAllPaidRecords(@PathVariable UUID debtId, @RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "10") int size) {
        Page<TransactionResponseDto> debts = paidService.getAllPaidRecords(debtId, page, size);
        return ResponseEntity.ok(new ApiResponse("Fetched debts", debts));
    }

    @GetMapping("/{debtId}/adjustment")
    @Operation(summary = "get all adjustment debt Record", description = "get all adjustment debt Record for the authenticated user")
    public ResponseEntity<ApiResponse> getAllAdjustmentRecords(@PathVariable UUID debtId, @RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "10") int size) {
        Page<TransactionResponseDto> debts = adjustmentService.getAllAdjustmentRecords(debtId, page, size);
        return ResponseEntity.ok(new ApiResponse("Fetched debts", debts));
    }

    @GetMapping("/{debtId}/received")
    @Operation(summary = "get all received debt Record", description = "get all received debt Record for the authenticated user")
    public ResponseEntity<ApiResponse> getAllReceivedRecords(@PathVariable UUID debtId, @RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "10") int size) {
        Page<TransactionResponseDto> debts = receivedService.getAllReceivedRecords(debtId, page, size);
        return ResponseEntity.ok(new ApiResponse("Fetched debts", debts));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete debt Record", description = "delete debt Record for the authenticated user")
    public ResponseEntity<ApiResponse> deleteRecord(@PathVariable UUID id) {
        try {
            transactionService.deleteTransactionById(id);
            return ResponseEntity.ok(new ApiResponse(" deleted", null));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{debtId}/total-paid")
    @Operation(summary = "Get total-paid")
    public ResponseEntity<ApiResponse> getTotalPaid(@PathVariable UUID debtId) {
        BigDecimal totalPaid = paidService.getTotalPaid(debtId);
        return ResponseEntity.ok(new ApiResponse("Fetched", totalPaid));
    }

    @GetMapping("/{debtId}/total-received")
    @Operation(summary = "Get total received")
    public ResponseEntity<ApiResponse> getTotalReceived(@PathVariable UUID debtId) {
        BigDecimal totalReceived = receivedService.getTotalReceived(debtId);
        return ResponseEntity.ok(new ApiResponse("Fetched", totalReceived));
    }
}
