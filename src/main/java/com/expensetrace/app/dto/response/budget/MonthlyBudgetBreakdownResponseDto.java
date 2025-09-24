package com.expensetrace.app.dto.response.budget;

import lombok.Data;

import java.util.List;

@Data
public class MonthlyBudgetBreakdownResponseDto extends BudgetResponseDto {
    private int month;
    private List<CategorySpendResponseDto> categories;
}
