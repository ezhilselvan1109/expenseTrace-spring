package com.expensetrace.app.dto.response.budget;

import lombok.Data;

import java.util.List;

@Data
public class BudgetListResponseDto {
    private List<BudgetResponseDto> past;
    private List<BudgetResponseDto> present;
    private List<BudgetResponseDto> upcoming;
}
