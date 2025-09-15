package com.expensetrace.app.service.transaction;

import java.util.UUID;

public interface TransactionService<ReqDto, ResDto> {
    ResDto create(ReqDto request);
    ResDto update(UUID id, ReqDto request);
}
