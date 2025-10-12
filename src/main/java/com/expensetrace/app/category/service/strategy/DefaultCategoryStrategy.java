package com.expensetrace.app.category.service.strategy;

import com.expensetrace.app.category.dto.response.CategoryResponseDto;

import java.util.List;
import java.util.UUID;

public interface DefaultCategoryStrategy {
    CategoryResponseDto setDefault(UUID categoryId);
    CategoryResponseDto getDefault();
    List<CategoryResponseDto> getAllCategories();
}
