package com.expensetrace.app.responseDto;

import lombok.Data;

import java.util.UUID;

@Data
public class TagResponseDto {
    private UUID id;
    private String name;
}
