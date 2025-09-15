package com.expensetrace.app.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Transaction Type: 1=EXPENSE, 2=INCOME, 3=TRANSFER, 4=ADJUSTMENT, 5=PAID, 6=RECEIVED, 7=DEBT_ADJUSTMENT")
public enum TransactionType {
    EXPENSE(1),
    INCOME(2),
    TRANSFER(3),
    ADJUSTMENT(4),
    DEBT_PAID(5),
    DEBT_RECEIVED(6),
    DEBT_ADJUSTMENT(7);

    private final int code;

    TransactionType(int code) {
        this.code = code;
    }

    @JsonValue
    public int getCode() {
        return code;
    }

    @JsonCreator
    public static TransactionType fromCode(int code) {
        for (TransactionType type : values()) {
            if (type.code == code) return type;
        }
        throw new IllegalArgumentException("Invalid Transaction Type code: " + code);
    }
}

