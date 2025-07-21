package com.expensetrace.app.requestDto;

import lombok.Data;
import java.util.List;

@Data
public class MonthlyBudgetRequestDto {
    private int year;
    private int month;
    private double totalLimit;
    private List<CategoryLimitDto> categoryLimits;
}
