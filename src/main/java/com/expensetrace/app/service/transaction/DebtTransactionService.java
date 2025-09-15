package com.expensetrace.app.service.transaction;

import java.util.UUID;

public interface DebtTransactionService<ReqDto, ResDto> {
    ResDto create(ReqDto request, UUID debtId);
    ResDto update(UUID id, ReqDto request);
}