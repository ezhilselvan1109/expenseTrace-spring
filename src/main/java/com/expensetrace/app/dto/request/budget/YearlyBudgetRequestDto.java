package com.expensetrace.app.dto.request.budget;

import com.expensetrace.app.dto.request.category.CategoryLimitDto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.List;

@Data
public class YearlyBudgetRequestDto extends BudgetRequestDto {

}
