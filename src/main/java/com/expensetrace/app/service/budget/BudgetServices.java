package com.expensetrace.app.service.budget;

import com.expensetrace.app.dto.request.budget.MonthlyBudgetRequestDto;
import com.expensetrace.app.dto.request.budget.YearlyBudgetRequestDto;
import com.expensetrace.app.dto.response.budget.BudgetListResponseDto;
import com.expensetrace.app.dto.response.budget.BudgetResponseDto;

import java.util.UUID;

public interface BudgetServices {
    BudgetListResponseDto getMonthlyBudgetList();
    BudgetListResponseDto getYearlyBudgetList();
    BudgetResponseDto createMonthlyBudget(MonthlyBudgetRequestDto dto);
    BudgetResponseDto createYearlyBudget(YearlyBudgetRequestDto dto);
    void deleteBudgetById(UUID budgetId);

    BudgetResponseDto updateMonthlyBudget(UUID budgetId, MonthlyBudgetRequestDto dto);
    BudgetResponseDto updateYearlyBudget(UUID budgetId, YearlyBudgetRequestDto dto);
    BudgetResponseDto getBudget(UUID budgetId);
}
