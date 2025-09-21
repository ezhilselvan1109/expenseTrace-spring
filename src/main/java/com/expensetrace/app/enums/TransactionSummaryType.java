package com.expensetrace.app.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Transaction Summary Type: 1=ALL_TIME, 2=MONTH, 3=YEAR")
public enum TransactionSummaryType {
    ALL_TIME(1),
    MONTH(2),
    YEAR(3);

    private final int code;

    TransactionSummaryType(int code) {
        this.code = code;
    }

    @JsonValue
    public int getCode() {
        return code;
    }

    @JsonCreator
    public static TransactionSummaryType fromCode(int code) {
        for (TransactionSummaryType type : values()) {
            if (type.code == code) return type;
        }
        throw new IllegalArgumentException("Invalid account type code: " + code);
    }
}
