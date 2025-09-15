package com.expensetrace.app.service.category.mapper;

import com.expensetrace.app.dto.request.category.CategoryRequestDto;
import com.expensetrace.app.dto.response.CategoryResponseDto;
import com.expensetrace.app.model.Category;
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

