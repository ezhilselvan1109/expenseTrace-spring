package com.expensetrace.app.dto.response.tag;

import lombok.Data;

import java.util.UUID;

@Data
public class TagResponseDto {
    private UUID id;
    private String name;
}
