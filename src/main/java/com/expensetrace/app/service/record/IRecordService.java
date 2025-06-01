package com.expensetrace.app.service.record;

import com.expensetrace.app.requestDto.RecordRequestDto;
import com.expensetrace.app.responseDto.RecordResponseDto;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface IRecordService {
    RecordResponseDto createRecord(UUID accountId, RecordRequestDto dto);
    List<RecordResponseDto> getAllRecordsByUser(UUID debtId);
    Page<RecordResponseDto> getAllRecordsByUser(UUID debtId, int page, int size);
    RecordResponseDto getRecordById(UUID id);
    void deleteRecordById(UUID id);
    RecordResponseDto updateRecord(UUID id, RecordRequestDto dto);

    Page<RecordResponseDto> getAllReceivedRecordsByUser(UUID debtId, int page, int size);

    Page<RecordResponseDto> getAllAdjustmentRecordsByUser(UUID debtId, int page, int size);

    Page<RecordResponseDto> getAllPaidRecordsByUser(UUID debtId, int page, int size);

    BigDecimal getTotalPaid(UUID debtId);

    BigDecimal getTotalReceived(UUID debtId);
}
