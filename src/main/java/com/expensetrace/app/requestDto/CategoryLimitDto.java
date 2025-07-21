package com.expensetrace.app.requestDto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CategoryLimitDto {
    private UUID categoryId;
    private double categoryLimit;
}
