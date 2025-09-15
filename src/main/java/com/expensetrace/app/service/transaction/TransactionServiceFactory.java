package com.expensetrace.app.service.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionServiceFactory {

    private final ApplicationContext context;

    public <ReqDto, ResDto> TransactionService<ReqDto, ResDto> getService(String key) {
        return (TransactionService<ReqDto, ResDto>) context.getBean(key);
    }

    public <ReqDto, ResDto> DebtTransactionService<ReqDto, ResDto> getDebtService(String key) {
        return (DebtTransactionService<ReqDto, ResDto>) context.getBean(key);
    }
}

