package com.expensetrace.app.service.category;

import com.expensetrace.app.requestDto.CategoryRequestDto;
import com.expensetrace.app.model.Category;
import com.expensetrace.app.responseDto.CategoryResponseDto;

import java.util.List;
import java.util.UUID;

public interface ICategoryService {
    CategoryResponseDto getCategoryById(UUID id);
    List<CategoryResponseDto> getAllCategories();
    CategoryResponseDto addCategory(CategoryRequestDto category);
    CategoryResponseDto updateCategory(CategoryRequestDto category, UUID id);
    void deleteCategoryById(UUID id);
}
