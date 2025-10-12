package com.expensetrace.app.category.dto.request;

import com.expensetrace.app.enums.CategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoryRequestDto {

    @NotBlank(message = "Category name is required")
    private String name;

    @NotNull(message = "Category type is required")
    private CategoryType type; // Enum: EXPENSE, INCOME

    @NotBlank(message = "Color is required")
    private String color;

    @NotBlank(message = "Icon is required")
    private String icon;
}
