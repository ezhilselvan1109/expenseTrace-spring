package com.expensetrace.app.service.transaction.impl.debt;

import com.expensetrace.app.dto.request.transaction.record.DebtAdjustmentRequestDto;
import com.expensetrace.app.dto.response.transaction.TransferTransactionResponseDto;
import com.expensetrace.app.dto.response.transaction.record.DebtAdjustmentResponseDto;
import com.expensetrace.app.model.debt.Debt;
import com.expensetrace.app.model.transaction.record.AdjustmentRecord;
import com.expensetrace.app.repository.DebtRepository;
import com.expensetrace.app.repository.transaction.debt.AdjustmentRecordRepository;
import com.expensetrace.app.service.transaction.DebtTransactionService;
import com.expensetrace.app.service.transaction.TransactionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service("adjustmentRecordService")
@RequiredArgsConstructor
public class DebtAdjustmentRecordServiceImpl  implements DebtTransactionService<DebtAdjustmentRequestDto, DebtAdjustmentResponseDto> {
    private final AdjustmentRecordRepository repository;
    private final ModelMapper mapper;
    private final DebtRepository debtRepository;

    @Override
    public DebtAdjustmentResponseDto create(DebtAdjustmentRequestDto request, UUID debtId) {
        AdjustmentRecord transaction = new AdjustmentRecord();
        Debt debt = debtRepository.findById(debtId)
                .orElseThrow(() -> new RuntimeException("Debt not found"));
        transaction.setDebt(debt);
        transaction.setTxnDate(request.getTxnDate());
        transaction.setTxnTime(request.getTxnTime());
        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());

        AdjustmentRecord saved = repository.save(transaction);

        return mapper.map(saved, DebtAdjustmentResponseDto.class);
    }

    @Override
    public DebtAdjustmentResponseDto update(UUID id, DebtAdjustmentRequestDto request) {
        AdjustmentRecord transaction = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setTxnDate(request.getTxnDate());
        transaction.setTxnTime(request.getTxnTime());
        AdjustmentRecord saved = repository.save(transaction);
        return mapper.map(saved, DebtAdjustmentResponseDto.class);
    }
}
