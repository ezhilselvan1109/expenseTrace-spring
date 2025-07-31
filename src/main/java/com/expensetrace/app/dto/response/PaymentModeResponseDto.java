package com.expensetrace.app.dto.response;

import com.expensetrace.app.enums.PaymentModeType;
import lombok.Data;

@Data
public class PaymentModeResponseDto {
    private String name;
    private PaymentModeType type;
}