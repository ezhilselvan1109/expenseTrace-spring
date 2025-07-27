package com.expensetrace.app.responseDto.summary;

import com.expensetrace.app.responseDto.CategoryResponseDto;
import lombok.Data;

@Data
public class CategorySummary {
    private CategoryResponseDto category;
    private Integer amount;
}
