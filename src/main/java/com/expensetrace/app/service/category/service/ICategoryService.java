package com.expensetrace.app.service.category.service;

import com.expensetrace.app.dto.request.category.CategoryRequestDto;
import com.expensetrace.app.dto.response.CategoryResponseDto;
import com.expensetrace.app.model.User;

import java.util.UUID;

public interface ICategoryService {
    CategoryResponseDto getCategoryById(UUID id);
    CategoryResponseDto addCategory(CategoryRequestDto category);
    CategoryResponseDto updateCategory(CategoryRequestDto category, UUID id);
    void deleteCategoryById(UUID id);
    void createDefaultCategoriesForUser(User user);
}
