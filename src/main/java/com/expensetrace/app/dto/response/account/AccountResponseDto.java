package com.expensetrace.app.dto.response.account;

import com.expensetrace.app.enums.AccountType;
import lombok.Data;

import java.util.UUID;

@Data
public class AccountResponseDto {
    private UUID id;
    private String name;
    private AccountType type;
    private boolean isDefault;
}