package com.expensetrace.app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class MonthlyBudgetBreakdownResponseDto {
    private UUID budgetId;
    private int month;
    private int year;
    private double budget;
    private double totalSpent;
    private List<CategorySpendResponseDto> categories;
}
