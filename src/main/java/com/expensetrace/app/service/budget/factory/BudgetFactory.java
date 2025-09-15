package com.expensetrace.app.service.budget.factory;

import com.expensetrace.app.dto.request.budget.BudgetRequestDto;
import com.expensetrace.app.dto.request.budget.MonthlyBudgetRequestDto;
import com.expensetrace.app.dto.request.budget.YearlyBudgetRequestDto;
import com.expensetrace.app.repository.MonthlyBudgetRepository;
import com.expensetrace.app.repository.YearlyBudgetRepository;
import com.expensetrace.app.service.budget.strategy.BudgetStrategy;
import com.expensetrace.app.service.budget.strategy.MonthlyBudgetStrategy;
import com.expensetrace.app.service.budget.strategy.YearlyBudgetStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BudgetFactory {

    private final MonthlyBudgetStrategy monthlyStrategy;
    private final YearlyBudgetStrategy yearlyStrategy;
    private final MonthlyBudgetRepository monthlyRepo;
    private final YearlyBudgetRepository yearlyRepo;

    public BudgetStrategy getStrategy(BudgetRequestDto dto) {
        if (dto instanceof MonthlyBudgetRequestDto) return monthlyStrategy;
        if (dto instanceof YearlyBudgetRequestDto) return yearlyStrategy;
        throw new IllegalArgumentException("Unsupported budget type");
    }

    public BudgetStrategy getStrategy(String type) {
        if ("monthly".equalsIgnoreCase(type)) return monthlyStrategy;
        if ("yearly".equalsIgnoreCase(type)) return yearlyStrategy;
        throw new IllegalArgumentException("Unsupported budget type");
    }

    public BudgetStrategy getStrategyById(UUID id) {
        if (monthlyRepo.existsById(id)) return monthlyStrategy;
        if (yearlyRepo.existsById(id)) return yearlyStrategy;
        throw new IllegalArgumentException("No budget found with id " + id);
    }
}