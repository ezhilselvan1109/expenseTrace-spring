package com.expensetrace.app.controller;

import com.expensetrace.app.dto.request.category.CategoryRequestDto;
import com.expensetrace.app.dto.response.CategoryResponseDto;
import com.expensetrace.app.exception.AlreadyExistsException;
import com.expensetrace.app.exception.ResourceNotFoundException;
import com.expensetrace.app.response.ApiResponse;
import com.expensetrace.app.service.category.service.ICategoryService;
import com.expensetrace.app.service.category.strategy.DefaultCategoryStrategy;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/categories")
@Tag(name = "Category", description = "Operations related to user-defined categories")
public class CategoryController {

    private final ICategoryService categoryService;
    private final DefaultCategoryStrategy expenseDefaultStrategy;
    private final DefaultCategoryStrategy incomeDefaultStrategy;

    @GetMapping("/expense")
    @Operation(summary = "Get all categories")
    public ResponseEntity<ApiResponse> getAllExpenseCategories() {
        List<CategoryResponseDto> categories = expenseDefaultStrategy.getAllCategories();
        return ResponseEntity.ok(new ApiResponse("Expense categories retrieved successfully", categories));
    }

    @GetMapping("/income")
    @Operation(summary = "Get all categories")
    public ResponseEntity<ApiResponse> getAllCategories() {
        List<CategoryResponseDto> categories = incomeDefaultStrategy.getAllCategories();
        return ResponseEntity.ok(new ApiResponse("Income categories retrieved successfully", categories));
    }

    @PostMapping
    @Operation(summary = "Add a category")
    public ResponseEntity<ApiResponse> addCategory(@Valid @RequestBody CategoryRequestDto dto) {
        try {
            CategoryResponseDto created = categoryService.addCategory(dto);
            return ResponseEntity.status(CREATED).body(new ApiResponse("Category created", created));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by ID")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable UUID id) {
        try {
            CategoryResponseDto category = categoryService.getCategoryById(id);
            return ResponseEntity.ok(new ApiResponse("Category retrieved", category));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update category")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable UUID id,
                                                      @Valid @RequestBody CategoryRequestDto dto) {
        try {
            CategoryResponseDto updated = categoryService.updateCategory(dto, id);
            return ResponseEntity.ok(new ApiResponse("Category updated", updated));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete category by ID")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable UUID id) {
        try {
            categoryService.deleteCategoryById(id);
            return ResponseEntity.ok(new ApiResponse("Category deleted", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{id}/expense-default")
    public ResponseEntity<ApiResponse> setDefaultExpense(@PathVariable UUID id) {
        CategoryResponseDto updated = expenseDefaultStrategy.setDefault(id);
        return ResponseEntity.ok(new ApiResponse("Default expense updated", updated));
    }

    @GetMapping("/expense-default")
    public ResponseEntity<ApiResponse> getDefaultExpense() {
        CategoryResponseDto category = expenseDefaultStrategy.getDefault();
        return ResponseEntity.ok(new ApiResponse("Default expense retrieved", category));
    }

    @PutMapping("/{id}/income-default")
    public ResponseEntity<ApiResponse> setDefaultIncome(@PathVariable UUID id) {
        CategoryResponseDto updated = incomeDefaultStrategy.setDefault(id);
        return ResponseEntity.ok(new ApiResponse("Default income updated", updated));
    }

    @GetMapping("/income-default")
    public ResponseEntity<ApiResponse> getDefaultIncome() {
        CategoryResponseDto category = incomeDefaultStrategy.getDefault();
        return ResponseEntity.ok(new ApiResponse("Default income retrieved", category));
    }
}
