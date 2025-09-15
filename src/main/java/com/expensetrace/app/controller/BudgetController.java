package com.expensetrace.app.controller;

import com.expensetrace.app.dto.request.budget.MonthlyBudgetRequestDto;
import com.expensetrace.app.dto.request.budget.YearlyBudgetRequestDto;
import com.expensetrace.app.dto.response.budget.BudgetListResponseDto;
import com.expensetrace.app.dto.response.budget.BudgetResponseDto;
import com.expensetrace.app.service.budget.BudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping("/monthly")
    public ResponseEntity<BudgetResponseDto> createMonthly(
            @Valid @RequestBody MonthlyBudgetRequestDto dto) {
        return ResponseEntity.ok(budgetService.create(dto));
    }

    @PostMapping("/yearly")
    public ResponseEntity<BudgetResponseDto> createYearly(
            @Valid @RequestBody YearlyBudgetRequestDto dto) {
        return ResponseEntity.ok(budgetService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetResponseDto> getBudget(@PathVariable UUID id) {
        return ResponseEntity.ok(budgetService.getById(id));
    }

    @GetMapping("/monthly")
    public ResponseEntity<BudgetListResponseDto> getAllMonthly() {
        return ResponseEntity.ok(budgetService.getAll("monthly"));
    }

    @GetMapping("/yearly")
    public ResponseEntity<BudgetListResponseDto> getAllYearly() {
        return ResponseEntity.ok(budgetService.getAll("yearly"));
    }

    @PutMapping("/monthly/{id}")
    public ResponseEntity<BudgetResponseDto> updateMonthly(
            @PathVariable UUID id,
            @Valid @RequestBody MonthlyBudgetRequestDto dto) {
        return ResponseEntity.ok(budgetService.update("monthly", id, dto));
    }

    @PutMapping("/yearly/{id}")
    public ResponseEntity<BudgetResponseDto> updateYearly(
            @PathVariable UUID id,
            @Valid @RequestBody YearlyBudgetRequestDto dto) {
        return ResponseEntity.ok(budgetService.update("yearly", id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudget(@PathVariable UUID id) {
        budgetService.delete(id);
        return ResponseEntity.noContent().build();
    }
}