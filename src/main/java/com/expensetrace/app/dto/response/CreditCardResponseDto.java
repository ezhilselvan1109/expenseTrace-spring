package com.expensetrace.app.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CreditCardResponseDto extends AccountResponseDto{
    private BigDecimal currentAvailableLimit;
    private BigDecimal totalCreditLimit;
    private LocalDate billingCycleStartDate;
    private LocalDate paymentDueDate;
}
