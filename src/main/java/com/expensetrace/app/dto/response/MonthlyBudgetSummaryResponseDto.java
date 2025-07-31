package com.expensetrace.app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class MonthlyBudgetSummaryResponseDto {
    private UUID id;
    private int month;
    private int year;
    private double budget;
    private double totalSpent;
}
