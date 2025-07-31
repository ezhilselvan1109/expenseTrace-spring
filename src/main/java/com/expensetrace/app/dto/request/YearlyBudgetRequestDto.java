package com.expensetrace.app.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class YearlyBudgetRequestDto {
    private int year;
    private double totalLimit;
    private List<CategoryLimitDto> categoryLimits;
}
