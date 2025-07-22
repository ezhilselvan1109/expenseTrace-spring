package com.expensetrace.app.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class YearlyBudgetSummaryResponseDto {
    private UUID id;
    private int year;
    private double budget;
    private double totalSpent;
}
