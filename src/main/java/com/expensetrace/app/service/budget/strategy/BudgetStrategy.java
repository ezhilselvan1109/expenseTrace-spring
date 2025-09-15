package com.expensetrace.app.service.budget.strategy;

import com.expensetrace.app.dto.request.budget.BudgetRequestDto;
import com.expensetrace.app.dto.response.budget.BudgetListResponseDto;
import com.expensetrace.app.dto.response.budget.BudgetResponseDto;

import java.util.UUID;

public interface BudgetStrategy {
    BudgetResponseDto create(BudgetRequestDto dto);
    BudgetResponseDto get(UUID budgetId);
    BudgetListResponseDto getAll();
    BudgetResponseDto update(UUID id, BudgetRequestDto dto);
    void delete(UUID budgetId);
}
