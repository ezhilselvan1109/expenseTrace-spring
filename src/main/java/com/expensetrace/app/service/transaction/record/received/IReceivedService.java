package com.expensetrace.app.service.transaction.record.received;

import com.expensetrace.app.dto.request.transaction.record.ReceivedRequestDto;
import com.expensetrace.app.dto.response.transaction.TransactionResponseDto;
import com.expensetrace.app.model.transaction.Transaction;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.UUID;

public interface IReceivedService {
    TransactionResponseDto save(Transaction Transaction, ReceivedRequestDto dto);
    Page<TransactionResponseDto> getAllReceivedRecords(UUID debtId, int page, int size);

    BigDecimal getTotalReceived(UUID debtId);
}
