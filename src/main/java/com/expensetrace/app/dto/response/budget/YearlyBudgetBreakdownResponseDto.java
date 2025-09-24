package com.expensetrace.app.dto.response.budget;

import lombok.Data;

import java.util.List;

@Data
public class YearlyBudgetBreakdownResponseDto extends BudgetResponseDto {
    private List<CategorySpendResponseDto> categories;
}
