package com.expensetrace.app.dto.request.budget;

import com.expensetrace.app.dto.request.CategoryLimitDto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.List;

@Data
public class BudgetRequestDto {

    @Min(value = 2000, message = "Year must be 2000 or later")
    private int year;

    @Positive(message = "Total limit must be greater than zero")
    private double totalLimit;

    @NotNull(message = "Category limits list must not be null")
    private List<@NotNull CategoryLimitDto> categoryLimits;
}