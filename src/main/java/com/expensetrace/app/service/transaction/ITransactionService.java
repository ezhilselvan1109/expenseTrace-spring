package com.expensetrace.app.service.transaction;

import com.expensetrace.app.requestDto.TransactionRequestDTO;
import com.expensetrace.app.responseDto.TransactionResponseDTO;

import java.util.List;

public interface ITransactionService {
    TransactionResponseDTO createTransaction(TransactionRequestDTO dto);
    List<TransactionResponseDTO> getAllTransactionsByUser();
    TransactionResponseDTO getTransactionById(Long id);
    void deleteTransactionById(Long id);
    TransactionResponseDTO updateTransaction(Long id, TransactionRequestDTO dto);

}
