package com.expensetrace.app.service.transaction.record.adjustment;

import com.expensetrace.app.dto.request.transaction.record.AdjustmentRequestDto;
import com.expensetrace.app.dto.response.transaction.TransactionResponseDto;
import com.expensetrace.app.dto.response.transaction.record.AdjustmentResponseDto;
import com.expensetrace.app.enums.TransactionType;
import com.expensetrace.app.model.debt.Debt;
import com.expensetrace.app.model.transaction.Transaction;
import com.expensetrace.app.model.transaction.record.AdjustmentRecord;
import com.expensetrace.app.repository.transaction.record.AdjustmentRecordRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AdjustmentService implements IAdjustmentService {

    private final AdjustmentRecordRepository adjustmentRecordRepo;

    private final ModelMapper modelMapper;

    @Override
    public TransactionResponseDto save(Transaction transaction, AdjustmentRequestDto dto) {
        AdjustmentRecord txn = (AdjustmentRecord) transaction;
        txn.setDebt(new Debt() {{
            dto.getDebtId();
        }});
        Transaction saved = adjustmentRecordRepo.save(txn);
        return modelMapper.map(saved, AdjustmentResponseDto.class);
    }

    @Override
    public Page<TransactionResponseDto> getAllAdjustmentRecords(UUID debtId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending().and(Sort.by("time").descending()));

        Page<Transaction> debtPage = adjustmentRecordRepo.findByDebtIdAndType(debtId, TransactionType.ADJUSTMENT, pageable);

        return debtPage.map(debt -> modelMapper.map(debt, AdjustmentResponseDto.class));
    }

}
