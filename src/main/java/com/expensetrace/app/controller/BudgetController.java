package com.expensetrace.app.controller;

import com.expensetrace.app.requestDto.MonthlyBudgetRequestDto;
import com.expensetrace.app.requestDto.YearlyBudgetRequestDto;
import com.expensetrace.app.response.ApiResponse;
import com.expensetrace.app.responseDto.MonthlyBudgetBreakdownResponseDto;
import com.expensetrace.app.responseDto.YearlyBudgetBreakdownResponseDto;
import com.expensetrace.app.service.budget.IBudgetService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteBudget(@PathVariable UUID id) {
        budgetService.deleteBudget(id);
        return ResponseEntity.ok(new ApiResponse("Budget deleted successfully", null));
    }

    @PutMapping("/month/{id}")
    public ResponseEntity<ApiResponse> updateMonthlyBudget(
            @PathVariable UUID id,
            @RequestBody MonthlyBudgetRequestDto request) {
        budgetService.updateMonthlyBudget(id, request);
        return ResponseEntity.ok(new ApiResponse("Monthly budget updated successfully", null));
    }

    @PutMapping("/year/{id}")
    public ResponseEntity<ApiResponse> updateYearlyBudget(
            @PathVariable UUID id,
            @RequestBody YearlyBudgetRequestDto request) {
        budgetService.updateYearlyBudget(id, request);
        return ResponseEntity.ok(new ApiResponse("Yearly budget updated successfully", null));
    }

    @GetMapping("/summary/month")
    public ResponseEntity<?> getMonthlyBudgetSummary() {
        return ResponseEntity.ok(budgetService.getMonthlyBudgetSummary());
    }

    @GetMapping("/summary/year")
    public ResponseEntity<?> getYearlyBudgetSummary() {
        return ResponseEntity.ok(budgetService.getYearlyBudgetSummary());
    }


    @GetMapping("/monthly/{budgetId}")
    public ResponseEntity<MonthlyBudgetBreakdownResponseDto> getMonthlyBudgetBreakdown(@PathVariable UUID budgetId) {
        return ResponseEntity.ok(budgetService.getMonthlyBudgetBreakdown(budgetId));
    }

    @GetMapping("/yearly/{budgetId}")
    public ResponseEntity<YearlyBudgetBreakdownResponseDto> getYearlyBudgetBreakdown(@PathVariable UUID budgetId) {
        return ResponseEntity.ok(budgetService.getYearlyBudgetBreakdown(budgetId));
    }


}
