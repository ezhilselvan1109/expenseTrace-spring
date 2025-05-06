package com.expensetrace.app.requestDto;

import com.expensetrace.app.enums.PaymentModeType;
import lombok.Data;

@Data
public class PaymentModeRequestDto {

    private String name;

    private PaymentModeType type;
}
