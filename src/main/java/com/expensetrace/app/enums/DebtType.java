package com.expensetrace.app.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Debts Type: 1=LENDING, 2=BORROWING")
public enum DebtType {
    LENDING(1),
    BORROWING(2);

    private final int code;

    DebtType(int code) {
        this.code = code;
    }

    @JsonValue
    public int getCode() {
        return code;
    }

    @JsonCreator
    public static DebtType fromCode(int code) {
        for (DebtType type : values()) {
            if (type.code == code) return type;
        }
        throw new IllegalArgumentException("Invalid debts type code: " + code);
    }
}