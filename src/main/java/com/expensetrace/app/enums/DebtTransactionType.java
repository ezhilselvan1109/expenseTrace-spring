package com.expensetrace.app.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Debt Transaction Type: 1=PAID, 2=RECEIVED, 3=ADJUSTMENT")
public enum DebtTransactionType {
    PAID(1),
    RECEIVED(2),
    ADJUSTMENT(3);

    private final int code;

    DebtTransactionType(int code) {
        this.code = code;
    }

    @JsonValue
    public int getCode() {
        return code;
    }

    @JsonCreator
    public static DebtTransactionType fromCode(int code) {
        for (DebtTransactionType type : values()) {
            if (type.code == code) return type;
        }
        throw new IllegalArgumentException("Invalid account type code: " + code);
    }
}