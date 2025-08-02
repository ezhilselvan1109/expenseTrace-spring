package com.expensetrace.app.dto.request.account;

import com.expensetrace.app.enums.PaymentModeType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentModeRequestDto {

    @NotBlank(message = "Payment mode name must not be blank")
    private String name;

    @NotNull(message = "Payment mode type is required")
    private PaymentModeType type;
}
