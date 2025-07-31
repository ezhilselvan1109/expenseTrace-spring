package com.expensetrace.app.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class CategoryLimitDto {
    private UUID categoryId;
    private double categoryLimit;
}
