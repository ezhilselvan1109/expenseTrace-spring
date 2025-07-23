package com.expensetrace.app.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Decimal format: 1=DEFAULT, 2=NO_DECIMALS, 3=ONE_DECIMAL, 4=TWO_DECIMAL")
public enum DecimalFormatOption {
    DEFAULT(1),
    NO_DECIMALS(2),
    ONE_DECIMAL(3),
    TWO_DECIMAL(4);

    private final int code;

    DecimalFormatOption(int code) {
        this.code = code;
    }

    @JsonValue
    public int getCode() {
        return code;
    }

    @JsonCreator
    public static DecimalFormatOption fromCode(int code) {
        for (DecimalFormatOption option : values()) {
            if (option.code == code) return option;
        }
        throw new IllegalArgumentException("Invalid decimal format code: " + code);
    }
}
