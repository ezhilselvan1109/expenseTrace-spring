package com.expensetrace.app.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Time format: 1=12_HOUR, 2=24_HOUR")
public enum TimeFormat {
    TWELVE_HOUR(1),
    TWENTY_FOUR_HOUR(2);

    private final int code;

    TimeFormat(int code) {
        this.code = code;
    }

    @JsonValue
    public int getCode() {
        return code;
    }

    @JsonCreator
    public static TimeFormat fromCode(int code) {
        for (TimeFormat format : values()) {
            if (format.code == code) return format;
        }
        throw new IllegalArgumentException("Invalid time format code: " + code);
    }
}
