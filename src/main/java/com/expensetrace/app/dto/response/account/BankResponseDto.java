package com.expensetrace.app.dto.response.account;

import com.expensetrace.app.dto.request.account.PaymentModeRequestDto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class BankResponseDto extends AccountResponseDto {
    private BigDecimal currentBalance;
    private List<PaymentModeResponseDto> linkedPaymentModes;
}
