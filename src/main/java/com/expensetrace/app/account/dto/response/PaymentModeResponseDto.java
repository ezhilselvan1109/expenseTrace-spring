package com.expensetrace.app.account.dto.response;

import com.expensetrace.app.enums.PaymentModeType;
import lombok.Data;

import java.util.UUID;

@Data
public class PaymentModeResponseDto {
    private UUID id;
    private String name;
    private PaymentModeType type;
}