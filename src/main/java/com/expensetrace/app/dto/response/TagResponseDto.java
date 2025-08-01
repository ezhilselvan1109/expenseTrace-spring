package com.expensetrace.app.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class TagResponseDto {
    private UUID id;
    private String name;
    private Integer transactions=0;
}
