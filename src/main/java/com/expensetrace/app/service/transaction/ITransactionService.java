package com.expensetrace.app.service.transaction;

import com.expensetrace.app.enums.TransactionType;
import com.expensetrace.app.requestDto.TransactionRequestDTO;
import com.expensetrace.app.responseDto.TransactionResponseDTO;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface ITransactionService {
    TransactionResponseDTO createTransaction(TransactionRequestDTO dto);
    List<TransactionResponseDTO> getAllTransactionsByUser();
    Page<TransactionResponseDTO> getAllTransactionsByUser(int page, int size);
    TransactionResponseDTO getTransactionById(UUID id);
    void deleteTransactionById(UUID id);
    TransactionResponseDTO updateTransaction(UUID id, TransactionRequestDTO dto);
    Page<TransactionResponseDTO> searchTransactions(
            TransactionType type, UUID accountId, UUID categoryId, UUID tagId, String description,
            BigDecimal minAmount, BigDecimal maxAmount,
            String dateFrom, String dateTo,
            int page, int size
    );
}
