package com.expensetrace.app.controller;

import com.expensetrace.app.requestDto.MonthlyBudgetRequestDto;
import com.expensetrace.app.requestDto.YearlyBudgetRequestDto;
import com.expensetrace.app.response.ApiResponse;
import com.expensetrace.app.service.budget.IBudgetService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/budget")
@Tag(name = "Budget", description = "Operations related to budgets")
public class BudgetController {

    private final IBudgetService budgetService;

    @PostMapping("/month")
    public ResponseEntity<ApiResponse> createMonthlyBudget(@RequestBody MonthlyBudgetRequestDto requestDto) {
        budgetService.createMonthlyBudget(requestDto);
        return ResponseEntity.status(CREATED).body(new ApiResponse("Monthly budget created successfully", null));
    }

    @PostMapping("/year")
    public ResponseEntity<ApiResponse> createYearlyBudget(@RequestBody YearlyBudgetRequestDto requestDto) {
        budgetService.createYearlyBudget(requestDto);
        return ResponseEntity.status(CREATED).body(new ApiResponse("Yearly budget created successfully", null));
    }
}
