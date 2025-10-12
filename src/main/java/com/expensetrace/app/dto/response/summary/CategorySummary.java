package com.expensetrace.app.dto.response.summary;

import com.expensetrace.app.category.dto.response.CategoryResponseDto;
import lombok.Data;

@Data
public class CategorySummary {
    private CategoryResponseDto category;
    private Integer amount;
}
