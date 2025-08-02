package com.expensetrace.app.service.transaction;

import com.expensetrace.app.dto.request.transaction.TransactionRequestDto;
import com.expensetrace.app.dto.response.transaction.TransactionResponseDto;

import java.util.UUID;

public interface ITransactionService {
    TransactionResponseDto createTransaction(TransactionRequestDto dto);
    void deleteTransactionById(UUID id);
}
