package com.expensetrace.app.dto.request.account;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AccountRequestDto {

    @NotBlank(message = "Account name is required")
    private String name;
}
