package com.expensetrace.app.service.budget;

import com.expensetrace.app.requestDto.MonthlyBudgetRequestDto;
import com.expensetrace.app.requestDto.YearlyBudgetRequestDto;
import jakarta.transaction.Transactional;

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
}
