package com.expensetrace.app.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.List;

@Data
public class YearlyBudgetRequestDto {

    @Min(value = 2000, message = "Year must be 2000 or later")
    private int year;

    @Positive(message = "Total limit must be greater than zero")
    private double totalLimit;

    @NotNull(message = "Category limits list must not be null")
    private List<@NotNull CategoryLimitDto> categoryLimits;
}
