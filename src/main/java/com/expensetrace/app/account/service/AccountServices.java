package com.expensetrace.app.account.service;

import com.expensetrace.app.account.dto.request.AccountRequestDto;
import com.expensetrace.app.account.dto.response.AccountResponseDto;

import java.util.List;
import java.util.UUID;

public interface AccountServices<
        T extends AccountRequestDto,
        R extends AccountResponseDto> {
    R create(T request);

    R update(T request,UUID id);

    List<R> getAll();
}
