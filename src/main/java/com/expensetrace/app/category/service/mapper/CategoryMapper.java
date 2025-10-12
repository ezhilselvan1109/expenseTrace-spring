package com.expensetrace.app.category.service.mapper;

import com.expensetrace.app.category.dto.request.CategoryRequestDto;
import com.expensetrace.app.category.dto.response.CategoryResponseDto;
import com.expensetrace.app.category.model.Category;
import com.expensetrace.app.model.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryMapper {
    private final ModelMapper modelMapper;

    public CategoryResponseDto toDto(Category category) {
        return modelMapper.map(category, CategoryResponseDto.class);
    }

    public Category toEntity(CategoryRequestDto dto, User user) {
        Category category = modelMapper.map(dto, Category.class);
        category.setUser(user);
        category.setDeletable(true);
        return category;
    }
}

