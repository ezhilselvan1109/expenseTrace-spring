package com.expensetrace.app.service.transaction.income;

import com.expensetrace.app.dto.request.transaction.IncomeTransactionRequestDto;
import com.expensetrace.app.dto.response.transaction.TransactionResponseDto;
import com.expensetrace.app.model.transaction.IncomeTransaction;
import com.expensetrace.app.model.transaction.Transaction;
import org.springframework.data.domain.Page;

public interface IIncomeTransactionService {
    TransactionResponseDto save(IncomeTransaction transaction, IncomeTransactionRequestDto dto);

    Page<TransactionResponseDto> getAllIncomeTransactions(int page, int size);
}
