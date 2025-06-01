package com.expensetrace.app.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Budget Type: 1=MONTHLY, 2=YEARLY")
public enum BudgetType {
    MONTHLY(1),
    YEARLY(2);
    private final int code;

    BudgetType(int code) {
        this.code = code;
    }

    @JsonValue
    public int getCode() {
        return code;
    }

    @JsonCreator
    public static BudgetType fromCode(int code) {
        for (BudgetType type : values()) {
            if (type.code == code) return type;
        }
        throw new IllegalArgumentException("Invalid Budget Type code: " + code);
    }
}
