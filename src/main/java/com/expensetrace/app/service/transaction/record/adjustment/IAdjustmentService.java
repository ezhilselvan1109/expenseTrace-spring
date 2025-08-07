package com.expensetrace.app.service.transaction.record.adjustment;

import com.expensetrace.app.dto.request.transaction.record.AdjustmentRequestDto;
import com.expensetrace.app.dto.request.transaction.record.PaidRequestDto;
import com.expensetrace.app.dto.response.transaction.TransactionResponseDto;
import com.expensetrace.app.model.transaction.Transaction;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface IAdjustmentService {
    TransactionResponseDto save(Transaction Transaction, AdjustmentRequestDto dto);
    Page<TransactionResponseDto> getAllAdjustmentRecords(UUID debtId, int page, int size);
}
