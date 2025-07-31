package com.expensetrace.app.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AccountRequestDto {

    @NotBlank(message = "Account name is required")
    private String name;
}
