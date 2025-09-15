package com.expensetrace.app.service.category.strategy;

import com.expensetrace.app.dto.response.CategoryResponseDto;

import java.util.List;
import java.util.UUID;

public interface DefaultCategoryStrategy {
    CategoryResponseDto setDefault(UUID categoryId);
    CategoryResponseDto getDefault();
    List<CategoryResponseDto> getAllCategories();
}
