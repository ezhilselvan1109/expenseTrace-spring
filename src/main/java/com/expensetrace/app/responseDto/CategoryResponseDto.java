package com.expensetrace.app.responseDto;

import com.expensetrace.app.enums.CategoryType;
import lombok.Data;

@Data
public class CategoryResponseDto {
    private String name;
    private CategoryType type; // Enum: EXPENSE, INCOME
    private String color;
    private String icon;
}