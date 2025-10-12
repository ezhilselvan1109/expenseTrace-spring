package com.expensetrace.app.category.service.service;

import com.expensetrace.app.category.dto.request.CategoryRequestDto;
import com.expensetrace.app.category.dto.response.CategoryResponseDto;
import com.expensetrace.app.exception.AlreadyExistsException;
import com.expensetrace.app.exception.ResourceNotFoundException;
import com.expensetrace.app.category.model.Category;
import com.expensetrace.app.model.User;
import com.expensetrace.app.category.repository.CategoryRepository;
import com.expensetrace.app.category.service.factory.CategoryFactory;
import com.expensetrace.app.category.service.mapper.CategoryMapper;
import com.expensetrace.app.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;
    private final SecurityUtil securityUtil;
    private final CategoryMapper categoryMapper;
    private final CategoryFactory categoryFactory;

    @Override
    public CategoryResponseDto getCategoryById(UUID id) {
        UUID userId = securityUtil.getAuthenticatedUserId();
        Category category = categoryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found!"));
        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryResponseDto addCategory(CategoryRequestDto dto) {
        UUID userId = securityUtil.getAuthenticatedUserId();
        if (categoryRepository.existsByNameAndUserId(dto.getName(), userId)) {
            throw new AlreadyExistsException(dto.getName() + " already exists");
        }
        User user = new User();
        user.setId(userId);
        Category saved = categoryRepository.save(categoryMapper.toEntity(dto, user));
        return categoryMapper.toDto(saved);
    }

    @Override
    public CategoryResponseDto updateCategory(CategoryRequestDto dto, UUID id) {
        UUID userId = securityUtil.getAuthenticatedUserId();
        Category existing = categoryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found!"));

        existing.setName(dto.getName());
        existing.setType(dto.getType());
        existing.setIcon(dto.getIcon());
        existing.setColor(dto.getColor());

        Category updated = categoryRepository.save(existing);
        return categoryMapper.toDto(updated);
    }

    @Override
    public void deleteCategoryById(UUID id) {
        UUID userId = securityUtil.getAuthenticatedUserId();
        categoryRepository.findByIdAndUserId(id, userId)
                .ifPresentOrElse(categoryRepository::delete,
                        () -> { throw new ResourceNotFoundException("Category not found!"); });
    }

    @Override
    public void createDefaultCategoriesForUser(User user) {
        categoryRepository.saveAll(categoryFactory.createDefaultCategories(user));
    }
}
