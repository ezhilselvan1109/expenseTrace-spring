package com.expensetrace.app.dto.response.budget;

import com.expensetrace.app.dto.response.CategorySpendResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class YearlyBudgetBreakdownResponseDto extends BudgetResponseDto {
    private List<CategorySpendResponseDto> categories;
}
