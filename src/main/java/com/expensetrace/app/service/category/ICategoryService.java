package com.expensetrace.app.service.category;

import com.expensetrace.app.requestDto.CategoryRequestDto;
import com.expensetrace.app.model.Category;
import com.expensetrace.app.responseDto.CategoryResponseDto;

import java.util.List;

public interface ICategoryService {
    CategoryResponseDto getCategoryById(Long id);
    List<CategoryResponseDto> getAllCategories();
    CategoryResponseDto addCategory(CategoryRequestDto category);
    CategoryResponseDto updateCategory(CategoryRequestDto category, Long id);
    void deleteCategoryById(Long id);
}
