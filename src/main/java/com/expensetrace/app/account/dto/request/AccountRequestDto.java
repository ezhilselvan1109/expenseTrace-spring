package com.expensetrace.app.account.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AccountRequestDto {

    @NotBlank(message = "Account name is required")
    private String name;
}
