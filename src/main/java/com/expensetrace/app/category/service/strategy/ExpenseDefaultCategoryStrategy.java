package com.expensetrace.app.category.service.strategy;

import com.expensetrace.app.category.dto.response.CategoryResponseDto;
import com.expensetrace.app.category.service.mapper.CategoryMapper;
import com.expensetrace.app.category.enums.CategoryType;
import com.expensetrace.app.exception.ResourceNotFoundException;
import com.expensetrace.app.category.model.Category;
import com.expensetrace.app.category.repository.CategoryRepository;
import com.expensetrace.app.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service("expenseDefaultStrategy")
@RequiredArgsConstructor
public class ExpenseDefaultCategoryStrategy implements DefaultCategoryStrategy {

    private final CategoryRepository categoryRepository;
    private final SecurityUtil securityUtil;
    private final CategoryMapper mapper;

    @Override
    public List<CategoryResponseDto> getAllCategories() {
        UUID userId = securityUtil.getAuthenticatedUserId();
        return categoryRepository.findByUserIdAndType(userId,CategoryType.EXPENSE)
                .stream().map(mapper::toDto).toList();
    }

    @Override
    public CategoryResponseDto setDefault(UUID categoryId) {
        UUID userId = securityUtil.getAuthenticatedUserId();
        Category category = categoryRepository.findByIdAndUserId(categoryId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        if (!CategoryType.EXPENSE.equals(category.getType())) {
            throw new IllegalArgumentException("Not an expense category");
        }

        resetExistingDefaults(userId, CategoryType.EXPENSE);
        category.setDefault(true);
        categoryRepository.save(category);

        return mapper.toDto(category);
    }

    @Override
    public CategoryResponseDto getDefault() {
        UUID userId = securityUtil.getAuthenticatedUserId();
        Category category = categoryRepository.findByUserIdAndTypeAndIsDefaultTrue(userId, CategoryType.EXPENSE)
                .orElseThrow(() -> new ResourceNotFoundException("No default expense category"));
        return mapper.toDto(category);
    }

    private void resetExistingDefaults(UUID userId, CategoryType type) {
        categoryRepository.findByUserIdAndType(userId, type)
                .forEach(cat -> {
                    if (cat.isDefault()) {
                        cat.setDefault(false);
                        categoryRepository.save(cat);
                    }
                });
    }
}
