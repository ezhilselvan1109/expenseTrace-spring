package com.expensetrace.app.service.schedule;

import com.expensetrace.app.dto.request.schedule.ScheduleBaseRequestDto;
import com.expensetrace.app.dto.response.schedule.ScheduleBaseResponseDto;
import com.expensetrace.app.enums.schedule.ExecutionStatus;
import com.expensetrace.app.repository.schedule.ScheduledTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ScheduledTransactionBaseService {

    private final ScheduledTransactionRepository repository;
    private final ScheduledTransactionFactory factory;
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public ScheduleBaseResponseDto getById(UUID id) {
        return repository.findById(id)
                .map(tx -> factory.<ScheduleBaseRequestDto, ScheduleBaseResponseDto>
                                getService(tx.getType())
                        .toResponse(tx))
                .orElse(null);
    }

    public Page<ScheduleBaseResponseDto> getAllByExecutionStatus(ExecutionStatus status, int page, int size) {
        return repository.findByStatus(status, PageRequest.of(page, size))
                .map(tx -> factory.<ScheduleBaseRequestDto, ScheduleBaseResponseDto>
                                getService(tx.getType())
                        .toResponse(tx));
    }
}
