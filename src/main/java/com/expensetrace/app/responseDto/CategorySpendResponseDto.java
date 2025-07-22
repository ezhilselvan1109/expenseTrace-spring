package com.expensetrace.app.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.UUID;

@Data
@AllArgsConstructor
public class CategorySpendResponseDto {
    private UUID categoryId;
    private String name;
    private String color;
    private String icon;
    private double limit;
    private double spent;
}
