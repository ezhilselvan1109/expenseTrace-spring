package com.expensetrace.app.controller;

import com.expensetrace.app.requestDto.PaymentModeRequestDto;
import com.expensetrace.app.exception.AlreadyExistsException;
import com.expensetrace.app.exception.ResourceNotFoundException;
import com.expensetrace.app.response.ApiResponse;
import com.expensetrace.app.responseDto.PaymentModeResponseDto;
import com.expensetrace.app.service.paymentMode.IPaymentModeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/accounts/payment-modes")
@Tag(name = "PaymentMode", description = "Manage your PaymentMode")
public class PaymentModeController {
    private final IPaymentModeService paymentModeService;

    @GetMapping("/{id}/all")
    public ResponseEntity<ApiResponse> getAllPaymentModesByAccount(@PathVariable Long id) {
        try {
            List<PaymentModeResponseDto> paymentModeRequestDto = paymentModeService.getAllPaymentModesByAccount(id);
            return  ResponseEntity.ok(new ApiResponse("Found!", paymentModeRequestDto));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error:", INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllPaymentModes() {
        try {
            List<PaymentModeResponseDto> paymentModeRequestDto = paymentModeService.getAllPaymentModes();
            return  ResponseEntity.ok(new ApiResponse("Found!", paymentModeRequestDto));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error:", INTERNAL_SERVER_ERROR));
        }
    }

    @PostMapping("/{id}/add")
    public ResponseEntity<ApiResponse> addPaymentMode(@PathVariable Long id,@Valid @RequestBody PaymentModeRequestDto paymentModeRequestDto) {
        try {
            PaymentModeResponseDto thePaymentMode = paymentModeService.addPaymentMode(id,paymentModeRequestDto);
            return  ResponseEntity.ok(new ApiResponse("Success", thePaymentMode));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @Operation(summary = "Get paymentMode by ID", description = "Returns a paymentMode by their ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getPaymentModeById(@PathVariable Long id){
        try {
            PaymentModeResponseDto thePaymentMode = paymentModeService.getPaymentModeById(id);
            return  ResponseEntity.ok(new ApiResponse("Found", thePaymentMode));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deletePaymentMode(@PathVariable Long id){
        try {
            paymentModeService.deletePaymentModeById(id);
            return  ResponseEntity.ok(new ApiResponse("Found", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updatePaymentMode(@PathVariable Long id, @RequestBody PaymentModeRequestDto paymentModeRequestDto) {
        try {
            PaymentModeResponseDto updatedPaymentMode = paymentModeService.updatePaymentMode(paymentModeRequestDto, id);
            return ResponseEntity.ok(new ApiResponse("Update success!", updatedPaymentMode));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
