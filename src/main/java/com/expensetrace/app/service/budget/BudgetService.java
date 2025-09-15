package com.expensetrace.app.service.budget;

import com.expensetrace.app.dto.request.budget.BudgetRequestDto;
import com.expensetrace.app.dto.response.budget.BudgetListResponseDto;
import com.expensetrace.app.dto.response.budget.BudgetResponseDto;

import java.util.UUID;

public interface BudgetService {
    BudgetResponseDto create(BudgetRequestDto dto);
    BudgetResponseDto getById(UUID budgetId);
    BudgetListResponseDto getAll(String type);
    BudgetResponseDto update(String type, UUID id, BudgetRequestDto dto);
    void delete(UUID budgetId);
}
