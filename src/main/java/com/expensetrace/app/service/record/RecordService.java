package com.expensetrace.app.service.record;

import com.expensetrace.app.enums.RecordType;
import com.expensetrace.app.exception.ResourceNotFoundException;
import com.expensetrace.app.model.Account;
import com.expensetrace.app.model.Debt;
import com.expensetrace.app.model.Record;
import com.expensetrace.app.repository.RecordsRepository;
import com.expensetrace.app.requestDto.RecordRequestDto;
import com.expensetrace.app.responseDto.RecordResponseDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecordService implements IRecordService {

    private final RecordsRepository recordRepo;
    private final ModelMapper modelMapper;

    public RecordResponseDto createRecord(UUID debtId, RecordRequestDto dto) {
        Debt debt=new Debt();
        debt.setId(debtId);
        Account account=new Account() {
            @Override
            public void setId(UUID id) {
                super.setId(id);
            }
        };
        account.setId(dto.getAccountId());

        Record record = new Record();
        record.setDebt(debt);
        record.setType(dto.getType());
        record.setDate(dto.getDate());
        record.setAccount(account);
        record.setAmount(dto.getAmount());
        record.setDescription(dto.getDescription());

        Record savedTxn = recordRepo.save(record);
        return modelMapper.map(savedTxn, RecordResponseDto.class);
    }

    public List<RecordResponseDto> getAllRecordsByUser(UUID debtId) {
        return recordRepo.findByDebtId(debtId).stream()
                .map(debt -> modelMapper.map(debt, RecordResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<RecordResponseDto> getAllRecordsByUser(UUID debtId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending().and(Sort.by("time").descending()));

        Page<Record> debtPage = recordRepo.findByDebtId(debtId, pageable);

        return debtPage.map(debt -> modelMapper.map(debt, RecordResponseDto.class));
    }


    public RecordResponseDto getRecordById(UUID id) {
        Record debt = recordRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found"));
        return modelMapper.map(debt, RecordResponseDto.class);
    }

    public void deleteRecordById(UUID id) {
        Record debt = recordRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found"));
        recordRepo.delete(debt);
    }

    public RecordResponseDto updateRecord(UUID id, RecordRequestDto dto) {
        Record record = recordRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found"));

        Account account=new Account() {
            @Override
            public void setId(UUID id) {
                super.setId(id);
            }
        };
        Debt debt=new Debt();
        debt.setId(id);
        record.setDebt(debt);
        record.setType(dto.getType());
        record.setDate(dto.getDate());
        record.setAmount(dto.getAmount());
        record.setAccount(account);
        record.setDescription(dto.getDescription());

        Record updated = recordRepo.save(record);
        return modelMapper.map(updated, RecordResponseDto.class);
    }

    @Override
    public Page<RecordResponseDto> getAllReceivedRecordsByUser(UUID debtId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending().and(Sort.by("time").descending()));

        Page<Record> debtPage = recordRepo.findByDebtIdAndType(debtId, RecordType.RECEIVED, pageable);

        return debtPage.map(debt -> modelMapper.map(debt, RecordResponseDto.class));
    }

    @Override
    public Page<RecordResponseDto> getAllAdjustmentRecordsByUser(UUID debtId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending().and(Sort.by("time").descending()));

        Page<Record> debtPage = recordRepo.findByDebtIdAndType(debtId, RecordType.ADJUSTMENT, pageable);

        return debtPage.map(debt -> modelMapper.map(debt, RecordResponseDto.class));
    }

    @Override
    public Page<RecordResponseDto> getAllPaidRecordsByUser(UUID debtId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending().and(Sort.by("time").descending()));

        Page<Record> debtPage = recordRepo.findByDebtIdAndType(debtId, RecordType.PAID, pageable);

        return debtPage.map(debt -> modelMapper.map(debt, RecordResponseDto.class));
    }
}
