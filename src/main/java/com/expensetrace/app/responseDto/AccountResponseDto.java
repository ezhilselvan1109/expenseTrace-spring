package com.expensetrace.app.responseDto;

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