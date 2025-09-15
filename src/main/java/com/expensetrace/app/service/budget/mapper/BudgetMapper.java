package com.expensetrace.app.service.budget.mapper;

import com.expensetrace.app.dto.request.budget.MonthlyBudgetRequestDto;
import com.expensetrace.app.dto.request.budget.YearlyBudgetRequestDto;
import com.expensetrace.app.dto.response.budget.*;
import com.expensetrace.app.model.budget.MonthlyBudget;
import com.expensetrace.app.model.budget.YearlyBudget;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BudgetMapper {

    private final ModelMapper modelMapper;

    public MonthlyBudget toEntity(MonthlyBudgetRequestDto dto) {
        return modelMapper.map(dto, MonthlyBudget.class);
    }

    public YearlyBudget toEntity(YearlyBudgetRequestDto dto) {
        return modelMapper.map(dto, YearlyBudget.class);
    }

    public MonthlyBudgetSummaryResponseDto toSummaryDto(MonthlyBudget budget, double spent) {
        MonthlyBudgetSummaryResponseDto dto = modelMapper.map(budget, MonthlyBudgetSummaryResponseDto.class);
        dto.setTotalSpent(spent);
        dto.setBudget(budget.getTotalLimit());
        return dto;
    }

    public YearlyBudgetSummaryResponseDto toSummaryDto(YearlyBudget budget, double spent) {
        YearlyBudgetSummaryResponseDto dto = modelMapper.map(budget, YearlyBudgetSummaryResponseDto.class);
        dto.setTotalSpent(spent);
        dto.setBudget(budget.getTotalLimit());
        return dto;
    }

    public MonthlyBudgetBreakdownResponseDto toBreakdownDto(MonthlyBudget budget, double spent) {
        MonthlyBudgetBreakdownResponseDto dto = modelMapper.map(budget, MonthlyBudgetBreakdownResponseDto.class);
        dto.setTotalSpent(spent);
        dto.setBudget(budget.getTotalLimit());
        return dto;
    }

    public YearlyBudgetBreakdownResponseDto toBreakdownDto(YearlyBudget budget, double spent) {
        YearlyBudgetBreakdownResponseDto dto = modelMapper.map(budget, YearlyBudgetBreakdownResponseDto.class);
        dto.setTotalSpent(spent);
        dto.setBudget(budget.getTotalLimit());
        return dto;
    }
}