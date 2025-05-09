package com.expensetrace.app.controller;

import com.expensetrace.app.requestDto.TransactionRequestDTO;
import com.expensetrace.app.model.Transaction;
import com.expensetrace.app.response.ApiResponse;
import com.expensetrace.app.service.transaction.TransactionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/transactions")
@Tag(name = "Transactions", description = "Manage your transactions")
public class TransactionController {

    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<ApiResponse> createTransaction(@ModelAttribute TransactionRequestDTO dto) {
        try {
            Transaction txn = transactionService.createTransaction(dto);
            return ResponseEntity.ok(new ApiResponse("Success",txn));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(),null));
        }
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> getTransactions(@PathVariable Long userId) {
        return ResponseEntity.ok(new ApiResponse("Success:",transactionService.getAllTransactionsByUser(userId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getTransaction(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse("Success",transactionService.getTransactionById(id)));
    }

    @PostMapping("/{transactionId}/attachment")
    public ResponseEntity<ApiResponse> uploadAttachment(
            @PathVariable Long transactionId,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            transactionService.uploadAttachment(transactionId, file);
            return ResponseEntity.ok(new ApiResponse("Attachment uploaded successfully",null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(),null));
        }
    }

}
