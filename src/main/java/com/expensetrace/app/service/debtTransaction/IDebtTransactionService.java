package com.expensetrace.app.service.debtTransaction;

import com.expensetrace.app.requestDto.DebtTransactionRequestDto;
import com.expensetrace.app.responseDto.DebtTransactionResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface IDebtTransactionService {
    DebtTransactionResponseDto createDebtTransaction(UUID accountId,DebtTransactionRequestDto dto);
    List<DebtTransactionResponseDto> getAllDebtTransactionsByUser(UUID debtId);
    Page<DebtTransactionResponseDto> getAllDebtTransactionsByUser(UUID debtId,int page, int size);
    DebtTransactionResponseDto getDebtTransactionById(UUID id);
    void deleteDebtTransactionById(UUID id);
    DebtTransactionResponseDto updateDebtTransaction(UUID id, DebtTransactionRequestDto dto);

    Page<DebtTransactionResponseDto> getAllReceivedDebtTransactionsByUser(UUID debtId,int page, int size);

    Page<DebtTransactionResponseDto> getAllAdjustmentDebtTransactionsByUser(UUID debtId,int page, int size);

    Page<DebtTransactionResponseDto> getAllPaidDebtTransactionsByUser(UUID debtId,int page, int size);
}
