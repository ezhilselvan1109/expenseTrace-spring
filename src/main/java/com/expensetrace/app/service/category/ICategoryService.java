package com.expensetrace.app.service.category;

import com.expensetrace.app.model.User;
import com.expensetrace.app.requestDto.CategoryRequestDto;
import com.expensetrace.app.responseDto.CategoryResponseDto;

import java.util.List;
import java.util.UUID;

public interface ICategoryService {
    CategoryResponseDto getCategoryById(UUID id);
    List<CategoryResponseDto> getAllCategories();
    CategoryResponseDto addCategory(CategoryRequestDto category);
    CategoryResponseDto updateCategory(CategoryRequestDto category, UUID id);
    void deleteCategoryById(UUID id);

    List<CategoryResponseDto> getAllIncomeCategories();

    List<CategoryResponseDto> getAllExpenseCategories();

    CategoryResponseDto getDefaultExpenseCategoryByUserId();

    CategoryResponseDto updateDefaultExpenseCategory(UUID categoryId);

    CategoryResponseDto getDefaultIncomeCategoryByUserId();

    CategoryResponseDto updateDefaultIncomeCategory(UUID categoryId);
    void createDefaultCategoriesForUser(User user);
}
