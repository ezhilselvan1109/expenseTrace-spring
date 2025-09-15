package com.expensetrace.app.service.budget.impl;

import com.expensetrace.app.dto.request.budget.BudgetRequestDto;
import com.expensetrace.app.dto.response.budget.BudgetListResponseDto;
import com.expensetrace.app.dto.response.budget.BudgetResponseDto;
import com.expensetrace.app.service.budget.BudgetService;
import com.expensetrace.app.service.budget.factory.BudgetFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {

    private final BudgetFactory factory;

    @Override
    public BudgetResponseDto create(BudgetRequestDto dto) {
        return factory.getStrategy(dto).create(dto);
    }

    @Override
    public BudgetResponseDto getById(UUID budgetId) {
        return factory.getStrategyById(budgetId).get(budgetId);
    }

    @Override
    public BudgetListResponseDto getAll(String type) {
        return factory.getStrategy(type).getAll();
    }

    @Override
    public BudgetResponseDto update(String type, UUID id, BudgetRequestDto dto) {
        return factory.getStrategy(type).update(id, dto);
    }

    @Override
    public void delete(UUID budgetId) {
        factory.getStrategyById(budgetId).delete(budgetId);
    }
}
