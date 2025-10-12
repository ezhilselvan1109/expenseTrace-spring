package com.expensetrace.app.category.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Category Type: 1=EXPENSE, 2=INCOME")
public enum CategoryType {
    EXPENSE(1),
    INCOME(2);
    private final int code;

    CategoryType(int code) {
        this.code = code;
    }

    @JsonValue
    public int getCode() {
        return code;
    }

    @JsonCreator
    public static CategoryType fromCode(int code) {
        for (CategoryType type : values()) {
            if (type.code == code) return type;
        }
        throw new IllegalArgumentException("Invalid account type code: " + code);
    }
}
