package com.expensetrace.app.service.transaction;

import com.expensetrace.app.dto.request.transaction.TransactionRequestDto;
import com.expensetrace.app.dto.response.transaction.TransactionResponseDto;
import com.expensetrace.app.enums.TransactionType;

import java.util.UUID;

public interface ITransactionService {
    TransactionResponseDto createTransaction(TransactionRequestDto dto);
    TransactionResponseDto updateTransaction(UUID id, TransactionRequestDto dto);
    void deleteTransactionById(UUID id);
    TransactionResponseDto getTransactionByIdAndType(UUID id, TransactionType type);
}
