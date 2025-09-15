package com.expensetrace.app.service.account;

import com.expensetrace.app.dto.request.account.AccountRequestDto;
import com.expensetrace.app.dto.request.schedule.ScheduleBaseRequestDto;
import com.expensetrace.app.dto.response.account.AccountResponseDto;
import com.expensetrace.app.dto.response.schedule.ScheduleBaseResponseDto;
import com.expensetrace.app.model.schedule.ScheduledTransaction;

import java.util.List;
import java.util.UUID;

public interface AccountServices<
        T extends AccountRequestDto,
        R extends AccountResponseDto> {
    R create(T request);

    R update(T request,UUID id);

    List<R> getAll();
}
