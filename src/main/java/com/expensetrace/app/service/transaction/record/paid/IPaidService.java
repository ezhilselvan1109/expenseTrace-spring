package com.expensetrace.app.service.transaction.record.paid;

import com.expensetrace.app.dto.request.transaction.record.PaidRequestDto;
import com.expensetrace.app.dto.response.transaction.TransactionResponseDto;
import com.expensetrace.app.model.transaction.Transaction;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.UUID;

public interface IPaidService {
    TransactionResponseDto save(Transaction Transaction, PaidRequestDto dto);
    Page<TransactionResponseDto> getAllPaidRecords(UUID debtId, int page, int size);

    BigDecimal getTotalPaid(UUID debtId);
}
