package com.expensetrace.app.controller;

import com.expensetrace.app.dto.request.CategoryRequestDto;
import com.expensetrace.app.exception.AlreadyExistsException;
import com.expensetrace.app.exception.ResourceNotFoundException;
import com.expensetrace.app.response.ApiResponse;
import com.expensetrace.app.dto.response.CategoryResponseDto;
import com.expensetrace.app.service.category.ICategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @GetMapping
    @Operation(summary = "Get all categories", description = "Retrieves all categories for the authenticated user")
    public ResponseEntity<ApiResponse> getAllCategories() {
        try {
            List<CategoryResponseDto> categories = categoryService.getAllCategories();
            return ResponseEntity.ok(new ApiResponse("Categories retrieved successfully", categories));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Internal server error", null));
        }
    }

    @GetMapping("/income")
    @Operation(summary = "Get all income categories", description = "Retrieves all income categories for the authenticated user")
    public ResponseEntity<ApiResponse> getAllIncomeCategories() {
        try {
            List<CategoryResponseDto> categories = categoryService.getAllIncomeCategories();
            return ResponseEntity.ok(new ApiResponse("Income Categories retrieved successfully", categories));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Internal server error", null));
        }
    }

    @GetMapping("/expense")
    @Operation(summary = "Get all expense categories", description = "Retrieves all expense categories for the authenticated user")
    public ResponseEntity<ApiResponse> getAllExpenseCategories() {
        try {
            List<CategoryResponseDto> categories = categoryService.getAllExpenseCategories();
            return ResponseEntity.ok(new ApiResponse("Expense Categories retrieved successfully", categories));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Internal server error", null));
        }
    }
    @PostMapping
    @Operation(summary = "Add a category", description = "Creates a new category for the authenticated user")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody CategoryRequestDto categoryRequestDto) {
        try {
            CategoryResponseDto createdCategory = categoryService.addCategory(categoryRequestDto);
            return ResponseEntity.status(CREATED).body(new ApiResponse("Category created successfully", createdCategory));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by ID", description = "Fetches a specific category owned by the user")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable UUID id) {
        try {
            CategoryResponseDto category = categoryService.getCategoryById(id);
            return ResponseEntity.ok(new ApiResponse("Category retrieved successfully", category));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete category by ID", description = "Deletes a category owned by the user")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable UUID id) {
        try {
            categoryService.deleteCategoryById(id);
            return ResponseEntity.ok(new ApiResponse("Category deleted successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update category", description = "Updates an existing category owned by the user")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable UUID id, @RequestBody CategoryRequestDto categoryRequestDto) {
        try {
            CategoryResponseDto updatedCategory = categoryService.updateCategory(categoryRequestDto, id);
            return ResponseEntity.ok(new ApiResponse("Category updated successfully", updatedCategory));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{id}/income-default")
    @Operation(summary = "Set default income category", description = "Update default income category for the authenticated user")
    public ResponseEntity<ApiResponse> updateDefaultIncomeCategory(@PathVariable UUID id) {
        try {
            CategoryResponseDto updated = categoryService.updateDefaultIncomeCategory(id);
            return ResponseEntity.ok(new ApiResponse("Update success!", updated));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/income-default")
    @Operation(summary = "Get default income category", description = "Retrieve the default income category for the authenticated user")
    public ResponseEntity<ApiResponse> getDefaultIncomeCategory() {
        try {
            CategoryResponseDto defaultAccount = categoryService.getDefaultIncomeCategoryByUserId();
            return ResponseEntity.ok(new ApiResponse("Found!", defaultAccount));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{id}/expense-default")
    @Operation(summary = "Set default expense category", description = "Update default expense category for the authenticated user")
    public ResponseEntity<ApiResponse> updateDefaultExpenseCategory(@PathVariable UUID id) {
        try {
            CategoryResponseDto updated = categoryService.updateDefaultExpenseCategory(id);
            return ResponseEntity.ok(new ApiResponse("Update success!", updated));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/expense-default")
    @Operation(summary = "Get default expense category", description = "Retrieve the default expense category for the authenticated user")
    public ResponseEntity<ApiResponse> getDefaultExpenseCategory() {
        try {
            CategoryResponseDto defaultAccount = categoryService.getDefaultExpenseCategoryByUserId();
            return ResponseEntity.ok(new ApiResponse("Found!", defaultAccount));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
