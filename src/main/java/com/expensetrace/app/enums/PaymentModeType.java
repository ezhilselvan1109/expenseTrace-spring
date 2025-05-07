package com.expensetrace.app.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Payment mode: 1=DEBIT_CARD, 2=UPI, 3=CHECK, 4=INTERNET_BANKING")
public enum PaymentModeType {
    DEBIT_CARD(1),
    UPI(2),
    CHECK(3),
    INTERNET_BANKING(4);

    private final int code;

    PaymentModeType(int code) {
        this.code = code;
    }

    @JsonValue
    public int getCode() {
        return code;
    }

    @JsonCreator
    public static PaymentModeType fromCode(int code) {
        for (PaymentModeType type : values()) {
            if (type.code == code) return type;
        }
        throw new IllegalArgumentException("Invalid payment mode code: " + code);
    }
}