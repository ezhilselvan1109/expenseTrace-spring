package com.expensetrace.app.responseDto;

import com.expensetrace.app.requestDto.PaymentModeRequestDto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class BankResponseDto extends AccountResponseDto{
    private BigDecimal currentBalance;
    private List<PaymentModeRequestDto> linkedPaymentModes;
}
