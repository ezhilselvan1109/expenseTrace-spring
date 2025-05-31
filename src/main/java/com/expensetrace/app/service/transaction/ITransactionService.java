package com.expensetrace.app.service.transaction;

import com.expensetrace.app.requestDto.TransactionRequestDTO;
import com.expensetrace.app.responseDto.TransactionResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface ITransactionService {
    TransactionResponseDTO createTransaction(TransactionRequestDTO dto);
    List<TransactionResponseDTO> getAllTransactionsByUser();
    Page<TransactionResponseDTO> getAllTransactionsByUser(int page, int size);
    TransactionResponseDTO getTransactionById(UUID id);
    void deleteTransactionById(UUID id);
    TransactionResponseDTO updateTransaction(UUID id, TransactionRequestDTO dto);
}
