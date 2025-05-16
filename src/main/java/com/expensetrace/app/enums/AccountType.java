package com.expensetrace.app.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Account type: 1=CASH, 2=BANK, 3=WALLET, 4=CREDIT_CARD")
public enum AccountType {
    BANK(1),
    WALLET(2),
    CREDIT_CARD(3),
    CASH(4);

    private final int code;

    AccountType(int code) {
        this.code = code;
    }

    @JsonValue
    public int getCode() {
        return code;
    }

    @JsonCreator
    public static AccountType fromCode(int code) {
        for (AccountType type : values()) {
            if (type.code == code) return type;
        }
        throw new IllegalArgumentException("Invalid account type code: " + code);
    }
}