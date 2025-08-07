package com.expensetrace.app.service.transaction.expense;

import com.expensetrace.app.dto.request.transaction.ExpenseTransactionRequestDto;
import com.expensetrace.app.dto.response.transaction.TransactionResponseDto;
import com.expensetrace.app.model.transaction.ExpenseTransaction;
import org.springframework.data.domain.Page;

public interface IExpenseTransactionService {
    TransactionResponseDto save(ExpenseTransaction transaction, ExpenseTransactionRequestDto dto);

    Page<TransactionResponseDto> getAllExpenseTransactions(int page, int size);
}
