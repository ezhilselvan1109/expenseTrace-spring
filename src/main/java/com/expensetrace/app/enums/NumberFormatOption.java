package com.expensetrace.app.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Number format: 1=MILLIONS, 2=LAKHS, 3=MILLIONS_COMPACT")
public enum NumberFormatOption {
    MILLIONS(1),
    LAKHS(2),
    MILLIONS_COMPACT(3);

    private final int code;

    NumberFormatOption(int code) {
        this.code = code;
    }

    @JsonValue
    public int getCode() {
        return code;
    }

    @JsonCreator
    public static NumberFormatOption fromCode(int code) {
        for (NumberFormatOption option : values()) {
            if (option.code == code) return option;
        }
        throw new IllegalArgumentException("Invalid number format code: " + code);
    }
}
