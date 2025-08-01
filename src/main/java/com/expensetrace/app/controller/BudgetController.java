package com.expensetrace.app.controller;

import com.expensetrace.app.dto.request.budget.MonthlyBudgetRequestDto;
import com.expensetrace.app.dto.request.budget.YearlyBudgetRequestDto;
import com.expensetrace.app.response.ApiResponse;
import com.expensetrace.app.dto.response.MonthlyBudgetBreakdownResponseDto;
import com.expensetrace.app.dto.response.YearlyBudgetBreakdownResponseDto;
import com.expensetrace.app.service.budget.IBudgetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/budget")
@Tag(name = "Budget", description = "Operations related to budgets (monthly/yearly) and summaries")
public class BudgetController {

    private final IBudgetService budgetService;

    @Operation(summary = "Create monthly budget", description = "Creates a new monthly budget for a user.")
    @PostMapping("/month")
    public ResponseEntity<ApiResponse> createMonthlyBudget(@Valid @RequestBody MonthlyBudgetRequestDto requestDto) {
        budgetService.createMonthlyBudget(requestDto);
        return ResponseEntity.status(CREATED).body(new ApiResponse("Monthly budget created successfully", null));
    }

    @Operation(summary = "Create yearly budget", description = "Creates a new yearly budget for a user.")
    @PostMapping("/year")
    public ResponseEntity<ApiResponse> createYearlyBudget(@Valid @RequestBody YearlyBudgetRequestDto requestDto) {
        budgetService.createYearlyBudget(requestDto);
        return ResponseEntity.status(CREATED).body(new ApiResponse("Yearly budget created successfully", null));
    }

    @Operation(summary = "Delete budget", description = "Deletes a budget (monthly/yearly) by its ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteBudget(@PathVariable UUID id) {
        budgetService.deleteBudget(id);
        return ResponseEntity.ok(new ApiResponse("Budget deleted successfully", null));
    }

    @Operation(summary = "Update monthly budget", description = "Updates a specific monthly budget by its ID.")
    @PutMapping("/month/{id}")
    public ResponseEntity<ApiResponse> updateMonthlyBudget(
            @PathVariable UUID id,
            @Valid @RequestBody MonthlyBudgetRequestDto request) {
        budgetService.updateMonthlyBudget(id, request);
        return ResponseEntity.ok(new ApiResponse("Monthly budget updated successfully", null));
    }

    @Operation(summary = "Update yearly budget", description = "Updates a specific yearly budget by its ID.")
    @PutMapping("/year/{id}")
    public ResponseEntity<ApiResponse> updateYearlyBudget(
            @PathVariable UUID id,
            @Valid @RequestBody YearlyBudgetRequestDto request) {
        budgetService.updateYearlyBudget(id, request);
        return ResponseEntity.ok(new ApiResponse("Yearly budget updated successfully", null));
    }

    @Operation(summary = "Get monthly budget summary", description = "Returns the total monthly budget and spending summary.")
    @GetMapping("/summary/month")
    public ResponseEntity<?> getMonthlyBudgetSummary() {
        return ResponseEntity.ok(budgetService.getMonthlyBudgetSummary());
    }

    @Operation(summary = "Get yearly budget summary", description = "Returns the total yearly budget and spending summary.")
    @GetMapping("/summary/year")
    public ResponseEntity<?> getYearlyBudgetSummary() {
        return ResponseEntity.ok(budgetService.getYearlyBudgetSummary());
    }

    @Operation(summary = "Get monthly budget breakdown", description = "Returns category-wise breakdown for a specific monthly budget.")
    @GetMapping("/monthly/{budgetId}")
    public ResponseEntity<MonthlyBudgetBreakdownResponseDto> getMonthlyBudgetBreakdown(@PathVariable UUID budgetId) {
        return ResponseEntity.ok(budgetService.getMonthlyBudgetBreakdown(budgetId));
    }

    @Operation(summary = "Get yearly budget breakdown", description = "Returns category-wise breakdown for a specific yearly budget.")
    @GetMapping("/yearly/{budgetId}")
    public ResponseEntity<YearlyBudgetBreakdownResponseDto> getYearlyBudgetBreakdown(@PathVariable UUID budgetId) {
        return ResponseEntity.ok(budgetService.getYearlyBudgetBreakdown(budgetId));
    }

}
