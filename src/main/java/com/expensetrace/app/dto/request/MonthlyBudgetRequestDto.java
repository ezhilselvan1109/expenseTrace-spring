package com.expensetrace.app.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.List;

@Data
public class MonthlyBudgetRequestDto {

    @Min(value = 2000, message = "Year must be no earlier than 2000")
    private int year;

    @Min(value = 1, message = "Month must be between 1 and 12")
    @Max(value = 12, message = "Month must be between 1 and 12")
    private int month;

    @Positive(message = "Total limit must be a positive value")
    private double totalLimit;

    @NotEmpty(message = "At least one category limit is required")
    @Valid
    private List<CategoryLimitDto> categoryLimits;
}
