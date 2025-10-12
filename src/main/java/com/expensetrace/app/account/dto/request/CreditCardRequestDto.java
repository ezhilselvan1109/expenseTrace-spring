package com.expensetrace.app.account.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CreditCardRequestDto extends AccountRequestDto {

    @NotNull(message = "Current available limit is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Available limit must be non-negative")
    private BigDecimal currentAvailableLimit;

    @NotNull(message = "Total credit limit is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Total credit limit must be non-negative")
    private BigDecimal totalCreditLimit;

    @NotNull(message = "Billing cycle start date is required")
    private LocalDate billingCycleStartDate;

    @NotNull(message = "Payment due date is required")
    private LocalDate paymentDueDate;
}
