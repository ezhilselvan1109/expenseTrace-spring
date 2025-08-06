package com.expensetrace.app.dto.request.category;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CategoryLimitDto {

    @NotNull(message = "Category ID is required")
    private UUID categoryId;

    @DecimalMin(value = "0.0", inclusive = false, message = "Category limit must be greater than 0")
    private double categoryLimit;
}
