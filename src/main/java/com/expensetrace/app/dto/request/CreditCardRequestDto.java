package com.expensetrace.app.dto.request;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CreditCardRequestDto extends AccountRequestDto {
    private BigDecimal currentAvailableLimit;
    private BigDecimal totalCreditLimit;
    private LocalDate billingCycleStartDate;
    private LocalDate paymentDueDate;
}
