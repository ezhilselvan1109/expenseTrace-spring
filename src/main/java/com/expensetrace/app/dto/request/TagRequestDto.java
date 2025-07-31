package com.expensetrace.app.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TagRequestDto {

    @NotBlank(message = "Tag name must not be blank")
    @Size(min = 2, max = 30, message = "Tag name must be between 2 and 30 characters")
    private String name;
}
