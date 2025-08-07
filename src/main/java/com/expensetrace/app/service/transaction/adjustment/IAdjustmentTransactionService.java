package com.expensetrace.app.service.transaction.adjustment;

import com.expensetrace.app.dto.request.transaction.AdjustmentTransactionRequestDto;
import com.expensetrace.app.dto.response.transaction.TransactionResponseDto;
import com.expensetrace.app.model.transaction.AdjustmentTransaction;
import org.springframework.data.domain.Page;

public interface IAdjustmentTransactionService {
    TransactionResponseDto save(AdjustmentTransaction transaction, AdjustmentTransactionRequestDto dto);

    Page<TransactionResponseDto> getAllAdjustmentTransactions(int page, int size);
}
