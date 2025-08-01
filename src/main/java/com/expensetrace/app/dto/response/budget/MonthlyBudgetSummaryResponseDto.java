package com.expensetrace.app.dto.response.budget;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
public class MonthlyBudgetSummaryResponseDto extends BudgetResponseDto {
    private int month;
}
