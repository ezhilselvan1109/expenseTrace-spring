package com.expensetrace.app.service.transaction.transfer;

import com.expensetrace.app.dto.request.transaction.TransferTransactionRequestDto;
import com.expensetrace.app.dto.response.transaction.TransactionResponseDto;
import com.expensetrace.app.model.transaction.TransferTransaction;
import org.springframework.data.domain.Page;

public interface ITransferTransactionService {
    TransactionResponseDto save(TransferTransaction Transaction, TransferTransactionRequestDto dto);

    Page<TransactionResponseDto> getAllTransferTransactions(int page, int size);
}
