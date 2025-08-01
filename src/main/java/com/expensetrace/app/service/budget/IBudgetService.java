package com.expensetrace.app.service.budget;

import com.expensetrace.app.dto.request.budget.MonthlyBudgetRequestDto;
import com.expensetrace.app.dto.request.budget.YearlyBudgetRequestDto;
import com.expensetrace.app.dto.response.budget.MonthlyBudgetBreakdownResponseDto;
import com.expensetrace.app.dto.response.budget.MonthlyBudgetSummaryResponseDto;
import com.expensetrace.app.dto.response.budget.YearlyBudgetBreakdownResponseDto;
import com.expensetrace.app.dto.response.budget.YearlyBudgetSummaryResponseDto;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IBudgetService {

    @Transactional
    void createMonthlyBudget(MonthlyBudgetRequestDto dto);

    @Transactional
    void createYearlyBudget(YearlyBudgetRequestDto dto);

    @Transactional
    void updateMonthlyBudget(UUID id, MonthlyBudgetRequestDto request);

    @Transactional
    void updateYearlyBudget(UUID id, YearlyBudgetRequestDto request);

    @Transactional
    void deleteBudget(UUID id);

    Map<String, List<MonthlyBudgetSummaryResponseDto>> getMonthlyBudgetSummary();

    Map<String, List<YearlyBudgetSummaryResponseDto>> getYearlyBudgetSummary();

    MonthlyBudgetBreakdownResponseDto getMonthlyBudgetBreakdown(UUID budgetId);

    YearlyBudgetBreakdownResponseDto getYearlyBudgetBreakdown(UUID budgetId);
}
