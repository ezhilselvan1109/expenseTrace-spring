package com.expensetrace.app.dto.response.budget;

import lombok.Data;

import java.util.UUID;

@Data
public class BudgetResponseDto {
    private UUID id;
    private int year;
    private double budget;
    private double totalSpent;
}
