package com.expensetrace.app.service.category;

import com.expensetrace.app.enums.CategoryType;
import com.expensetrace.app.model.Category;
import com.expensetrace.app.model.User;
import com.expensetrace.app.requestDto.CategoryRequestDto;
import com.expensetrace.app.exception.AlreadyExistsException;
import com.expensetrace.app.exception.ResourceNotFoundException;
import com.expensetrace.app.repository.CategoryRepository;
import com.expensetrace.app.responseDto.CategoryResponseDto;
import com.expensetrace.app.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;
    private final SecurityUtil securityUtil;
    private final ModelMapper modelMapper;
    @Override
    public CategoryResponseDto getCategoryById(UUID id) {
        UUID userId = securityUtil.getAuthenticatedUserId();
        Category category = categoryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found!"));
        return modelMapper.map(category, CategoryResponseDto.class);
    }


    @Override
    public List<CategoryResponseDto> getAllCategories() {
        UUID userId = securityUtil.getAuthenticatedUserId();
        return categoryRepository.findByUserId(userId).stream()
                .map(category -> modelMapper.map(category, CategoryResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponseDto addCategory(CategoryRequestDto categoryRequestDto) {
        UUID userId = securityUtil.getAuthenticatedUserId();
        if (categoryRepository.existsByNameAndUserId(categoryRequestDto.getName(),userId)) {
            throw new AlreadyExistsException(categoryRequestDto.getName() + " already exists");
        }

        Category category = modelMapper.map(categoryRequestDto, Category.class);
        User user=new User();
        user.setId(userId);
        category.setUser(user);

        Category savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryResponseDto.class);
    }
    @Override
    public CategoryResponseDto updateCategory(CategoryRequestDto categoryRequestDto, UUID id) {
        UUID userId = securityUtil.getAuthenticatedUserId();
        Category existing = categoryRepository.findByIdAndUserId(id,userId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found!"));

        existing.setName(categoryRequestDto.getName());
        existing.setType(categoryRequestDto.getType());
        existing.setIcon(categoryRequestDto.getIcon());
        existing.setColor(categoryRequestDto.getColor());

        Category updated = categoryRepository.save(existing);
        return modelMapper.map(updated, CategoryResponseDto.class);
    }

    @Override
    public void deleteCategoryById(UUID id) {
        UUID userId = securityUtil.getAuthenticatedUserId();
        categoryRepository.findByIdAndUserId(id,userId)
                .ifPresentOrElse(categoryRepository::delete, () -> {
                    throw new ResourceNotFoundException("Category not found!");
                });
    }

    @Override
    public List<CategoryResponseDto> getAllIncomeCategories() {
        UUID userId = securityUtil.getAuthenticatedUserId();
        return categoryRepository.findByUserIdAndType(userId, CategoryType.INCOME).stream()
                .map(category -> modelMapper.map(category, CategoryResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryResponseDto> getAllExpenseCategories() {
        UUID userId = securityUtil.getAuthenticatedUserId();
        return categoryRepository.findByUserIdAndType(userId, CategoryType.EXPENSE).stream()
                .map(category -> modelMapper.map(category, CategoryResponseDto.class))
                .collect(Collectors.toList());
    }
}
