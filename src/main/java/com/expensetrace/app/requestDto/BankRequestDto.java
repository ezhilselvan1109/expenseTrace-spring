package com.expensetrace.app.requestDto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class BankRequestDto extends AccountRequestDto{
    private BigDecimal currentBalance;
    private List<PaymentModeRequestDto> linkedPaymentModes;
}
