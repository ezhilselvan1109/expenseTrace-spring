package com.expensetrace.app.dto.response;

import com.expensetrace.app.dto.request.PaymentModeRequestDto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class BankResponseDto extends AccountResponseDto{
    private BigDecimal currentBalance;
    private List<PaymentModeRequestDto> linkedPaymentModes;
}
