package com.expensetrace.app.dto.response.budget;

import lombok.Data;

@Data
public class MonthlyBudgetResponseDto extends BudgetResponseDto {
    private int month;
}
