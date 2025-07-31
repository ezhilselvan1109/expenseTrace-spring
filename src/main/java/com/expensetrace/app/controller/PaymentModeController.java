package com.expensetrace.app.controller;

import com.expensetrace.app.dto.request.PaymentModeRequestDto;
import com.expensetrace.app.exception.AlreadyExistsException;
import com.expensetrace.app.exception.ResourceNotFoundException;
import com.expensetrace.app.response.ApiResponse;
import com.expensetrace.app.dto.response.PaymentModeResponseDto;
import com.expensetrace.app.service.paymentMode.IPaymentModeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/accounts/payment-modes")
@Tag(name = "PaymentMode", description = "Manage payment modes for user accounts")

public class PaymentModeController {

    private final IPaymentModeService paymentModeService;

    @Operation(
            summary = "Get all payment modes for an account",
            description = "Returns a list of all payment modes associated with a specific account by account ID"
    )
    @GetMapping("/{accountId}/all")
    public ResponseEntity<ApiResponse> getAllPaymentModesByAccount(@PathVariable UUID accountId) {
        try {
            List<PaymentModeResponseDto> paymentModeRequestDto = paymentModeService.getAllPaymentModesByAccount(accountId);
            return ResponseEntity.ok(new ApiResponse("Found!", paymentModeRequestDto));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error:", INTERNAL_SERVER_ERROR));
        }
    }

    @Operation(
            summary = "Get all payment modes",
            description = "Returns a list of all available payment modes across all accounts"
    )
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllPaymentModes() {
        try {
            List<PaymentModeResponseDto> paymentModeRequestDto = paymentModeService.getAllPaymentModes();
            return ResponseEntity.ok(new ApiResponse("Found!", paymentModeRequestDto));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error:", INTERNAL_SERVER_ERROR));
        }
    }

    @Operation(
            summary = "Add a new payment mode",
            description = "Creates a new payment mode for a specific account. Returns an error if it already exists."
    )
    @PostMapping("/{accountId}/add")
    public ResponseEntity<ApiResponse> addPaymentMode(@PathVariable UUID accountId, @Valid @RequestBody PaymentModeRequestDto paymentModeRequestDto) {
        try {
            PaymentModeResponseDto thePaymentMode = paymentModeService.addPaymentMode(accountId, paymentModeRequestDto);
            return ResponseEntity.ok(new ApiResponse("Success", thePaymentMode));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @Operation(
            summary = "Get payment mode by ID",
            description = "Returns a single payment mode by its ID"
    )
    @GetMapping("/{paymentModeId}")
    public ResponseEntity<ApiResponse> getPaymentModeById(@PathVariable UUID paymentModeId) {
        try {
            PaymentModeResponseDto thePaymentMode = paymentModeService.getPaymentModeById(paymentModeId);
            return ResponseEntity.ok(new ApiResponse("Found", thePaymentMode));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @Operation(
            summary = "Delete a payment mode",
            description = "Deletes a specific payment mode by its ID"
    )
    @DeleteMapping("/{paymentModeId}/delete")
    public ResponseEntity<ApiResponse> deletePaymentMode(@PathVariable UUID paymentModeId) {
        try {
            paymentModeService.deletePaymentModeById(paymentModeId);
            return ResponseEntity.ok(new ApiResponse("Deleted successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @Operation(
            summary = "Update a payment mode",
            description = "Updates the details of an existing payment mode by its ID"
    )
    @PutMapping("/{id}/update")
    public ResponseEntity<ApiResponse> updatePaymentMode(@PathVariable UUID id, @Valid @RequestBody PaymentModeRequestDto paymentModeRequestDto) {
        try {
            PaymentModeResponseDto updatedPaymentMode = paymentModeService.updatePaymentMode(paymentModeRequestDto, id);
            return ResponseEntity.ok(new ApiResponse("Update success!", updatedPaymentMode));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
