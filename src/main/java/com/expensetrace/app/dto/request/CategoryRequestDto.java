package com.expensetrace.app.dto.request;

import com.expensetrace.app.enums.CategoryType;
import lombok.Data;

@Data
public class CategoryRequestDto {
    private String name;
    private CategoryType type; // Enum: EXPENSE, INCOME
    private String color;
    private String icon;
}