package com.expensetrace.app.account.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class BankResponseDto extends AccountResponseDto {
    private BigDecimal currentBalance;
    private List<PaymentModeResponseDto> linkedPaymentModes;
}
