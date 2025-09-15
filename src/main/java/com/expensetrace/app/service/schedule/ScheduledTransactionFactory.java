package com.expensetrace.app.service.schedule;

import com.expensetrace.app.dto.request.schedule.ScheduleBaseRequestDto;
import com.expensetrace.app.dto.response.schedule.ScheduleBaseResponseDto;
import com.expensetrace.app.enums.schedule.ScheduleType;
import com.expensetrace.app.service.schedule.impl.ScheduledExpenseService;
import com.expensetrace.app.service.schedule.impl.ScheduledIncomeService;
import com.expensetrace.app.service.schedule.impl.ScheduledTransferService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ScheduledTransactionFactory {

    private final ScheduledExpenseService expenseService;
    private final ScheduledIncomeService incomeService;
    private final ScheduledTransferService transferService;

    private Map<ScheduleType, ScheduledTransactionService<?, ?>> serviceMap;

    @PostConstruct
    private void init() {
        serviceMap = Map.of(
                ScheduleType.EXPENSE, expenseService,
                ScheduleType.INCOME, incomeService,
                ScheduleType.TRANSFER, transferService
        );
    }

    @SuppressWarnings("unchecked")
    public <T extends ScheduleBaseRequestDto,
            R extends ScheduleBaseResponseDto>
    ScheduledTransactionService<T, R> getService(ScheduleType type) {
        return (ScheduledTransactionService<T, R>) serviceMap.get(type);
    }
}


