package com.expensetrace.app.service.budget;

import com.expensetrace.app.requestDto.MonthlyBudgetRequestDto;
import com.expensetrace.app.requestDto.YearlyBudgetRequestDto;

public interface IBudgetService {
    void createMonthlyBudget(MonthlyBudgetRequestDto requestDto);
    void createYearlyBudget(YearlyBudgetRequestDto requestDto);
}
