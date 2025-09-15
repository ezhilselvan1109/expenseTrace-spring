package com.expensetrace.app.dto.response;

import com.expensetrace.app.enums.CategoryType;
import lombok.Data;

import java.util.UUID;

@Data
public class CategoryResponseDto {
    private UUID id;
    private String name;
    private CategoryType type;
    private String color;
    private String icon;
    private boolean isDeletable;
}