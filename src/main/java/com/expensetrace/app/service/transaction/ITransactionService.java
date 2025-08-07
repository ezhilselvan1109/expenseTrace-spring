package com.expensetrace.app.service.transaction;

import com.expensetrace.app.dto.request.transaction.TransactionRequestDto;
import com.expensetrace.app.dto.response.transaction.TransactionResponseDto;
import com.expensetrace.app.enums.TransactionType;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface ITransactionService {
    TransactionResponseDto createTransaction(TransactionRequestDto dto);
    TransactionResponseDto createTransaction(UUID debtId,TransactionRequestDto dto);
    TransactionResponseDto updateTransaction(UUID id, TransactionRequestDto dto);
    void deleteTransactionById(UUID id);
    TransactionResponseDto getTransactionById(UUID id);
    Page<TransactionResponseDto> getAllTransactions(int page, int size);
    Page<TransactionResponseDto> getAllTransactions(int page, int size, TransactionType type);

    Page<TransactionResponseDto> getAllRecords(UUID debtId, int page, int size);
}
