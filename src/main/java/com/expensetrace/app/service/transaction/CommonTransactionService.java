package com.expensetrace.app.service.transaction;

import com.expensetrace.app.dto.response.transaction.*;
import com.expensetrace.app.dto.response.transaction.record.DebtAdjustmentResponseDto;
import com.expensetrace.app.dto.response.transaction.record.DebtPaidResponseDto;
import com.expensetrace.app.dto.response.transaction.record.DebtReceivedResponseDto;
import com.expensetrace.app.repository.transaction.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommonTransactionService {

    private final TransactionRepository repository;
    private final ModelMapper mapper;
    public TransactionResponseDto get(UUID id) {
        var txn = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found: " + id));

        switch (txn.getType()) {
            case INCOME:
                return mapper.map(txn, IncomeTransactionResponseDto.class);

            case EXPENSE:
                return mapper.map(txn, ExpenseTransactionResponseDto.class);

            case TRANSFER:
                return mapper.map(txn, TransferTransactionResponseDto.class);

            case ADJUSTMENT:
                return mapper.map(txn, AdjustmentTransactionResponseDto.class);

            case DEBT_PAID:
                return mapper.map(txn, DebtPaidResponseDto.class);

            case DEBT_RECEIVED:
                return mapper.map(txn, DebtReceivedResponseDto.class);

            case DEBT_ADJUSTMENT:
                return mapper.map(txn, DebtAdjustmentResponseDto.class);

            default:
                throw new IllegalStateException("Unsupported transaction type: " + txn.getType());
        }
    }

    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Transaction not found");
        }
        repository.deleteById(id);
    }
}
