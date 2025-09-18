package com.expensetrace.app.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "AnalysisType: 1=WEEK, 2=MONTH, 3=YEAR, 4=CUSTOM")
public enum AnalysisType {
    WEEK(1),
    MONTH(2),
    YEAR(3),
    CUSTOM(4);

    private final int code;

    AnalysisType(int code) {
        this.code = code;
    }

    @JsonValue
    public int getCode() {
        return code;
    }

    @JsonCreator
    public static AnalysisType fromCode(int code) {
        for (AnalysisType type : values()) {
            if (type.code == code) return type;
        }
        throw new IllegalArgumentException("Invalid account type code: " + code);
    }
}