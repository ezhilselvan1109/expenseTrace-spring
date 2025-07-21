package com.expensetrace.app.service.budget;

import com.expensetrace.app.requestDto.MonthlyBudgetRequestDto;
import com.expensetrace.app.requestDto.YearlyBudgetRequestDto;

import java.util.UUID;

public interface IBudgetService {
    void createMonthlyBudget(MonthlyBudgetRequestDto requestDto);
    void createYearlyBudget(YearlyBudgetRequestDto requestDto);
    void deleteBudget(UUID id);
}
