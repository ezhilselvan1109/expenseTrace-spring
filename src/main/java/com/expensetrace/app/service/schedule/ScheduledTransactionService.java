package com.expensetrace.app.service.schedule;

import com.expensetrace.app.dto.request.schedule.ScheduleBaseRequestDto;
import com.expensetrace.app.dto.response.schedule.ScheduleBaseResponseDto;
import com.expensetrace.app.model.schedule.ScheduledTransaction;

import java.util.List;
import java.util.UUID;

public interface ScheduledTransactionService<
        T extends ScheduleBaseRequestDto,
        R extends ScheduleBaseResponseDto> {

    R create(T request);

    R update(UUID id, T request);

    R toResponse(ScheduledTransaction transaction);
}
