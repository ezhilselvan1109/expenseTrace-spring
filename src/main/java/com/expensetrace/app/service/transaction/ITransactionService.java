package com.expensetrace.app.service.transaction;

import com.expensetrace.app.requestDto.TransactionRequestDto;
import com.expensetrace.app.responseDto.TransactionResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface ITransactionService {
    TransactionResponseDto createTransaction(TransactionRequestDto dto);
    List<TransactionResponseDto> getAllTransactionsByUser();
    Page<TransactionResponseDto> getAllTransactionsByUser(int page, int size);
    TransactionResponseDto getTransactionById(UUID id);
    void deleteTransactionById(UUID id);
    TransactionResponseDto updateTransaction(UUID id, TransactionRequestDto dto);
}
