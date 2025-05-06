package com.expensetrace.app.service.category;

import com.expensetrace.app.requestDto.CategoryRequestDto;
import com.expensetrace.app.model.Category;

import java.util.List;

public interface ICategoryService {
    Category getCategoryById(Long id);
    Category getCategoryByName(String name);
    List<Category> getAllCategories();
    Category addCategory(CategoryRequestDto category);
    Category updateCategory(CategoryRequestDto category, Long id);
    void deleteCategoryById(Long id);
}
