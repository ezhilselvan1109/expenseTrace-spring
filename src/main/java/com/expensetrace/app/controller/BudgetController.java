package com.expensetrace.app.controller;

import com.expensetrace.app.dto.request.budget.MonthlyBudgetRequestDto;
import com.expensetrace.app.dto.request.budget.YearlyBudgetRequestDto;
import com.expensetrace.app.dto.response.budget.BudgetListResponseDto;
import com.expensetrace.app.dto.response.budget.BudgetResponseDto;
import com.expensetrace.app.service.budget.BudgetServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetServices budgetService;

    @GetMapping("/{budgetId}/summary")
    public ResponseEntity<BudgetResponseDto> getBudgetSummary(@PathVariable UUID budgetId) {
        return ResponseEntity.ok(budgetService.getBudget(budgetId));
    }
    @PostMapping("/monthly")
    public ResponseEntity<BudgetResponseDto> createMonthlyBudget(
            @RequestBody MonthlyBudgetRequestDto dto) {
        return ResponseEntity.ok(budgetService.createMonthlyBudget(dto));
    }

    @PostMapping("/yearly")
    public ResponseEntity<BudgetResponseDto> createYearlyBudget(
            @RequestBody YearlyBudgetRequestDto dto) {
        return ResponseEntity.ok(budgetService.createYearlyBudget(dto));
    }

    @GetMapping("/monthly")
    public ResponseEntity<BudgetListResponseDto> getMonthlyBudgets() {
        return ResponseEntity.ok(budgetService.getMonthlyBudgetList());
    }

    @GetMapping("/yearly")
    public ResponseEntity<BudgetListResponseDto> getYearlyBudgets() {
        return ResponseEntity.ok(budgetService.getYearlyBudgetList());
    }

    @DeleteMapping("/{budgetId}")
    public ResponseEntity<Void> deleteBudget(@PathVariable UUID budgetId) {
        budgetService.deleteBudgetById(budgetId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/monthly/{budgetId}")
    public ResponseEntity<BudgetResponseDto> updateMonthly(
            @PathVariable UUID budgetId,
            @RequestBody MonthlyBudgetRequestDto dto) {
        return ResponseEntity.ok(budgetService.updateMonthlyBudget(budgetId, dto));
    }

    @PutMapping("/yearly/{budgetId}")
    public ResponseEntity<BudgetResponseDto> updateYearly(
            @PathVariable UUID budgetId,
            @RequestBody YearlyBudgetRequestDto dto) {
        return ResponseEntity.ok(budgetService.updateYearlyBudget(budgetId, dto));
    }
}
