package com.expensetrace.app.responseDto;

import com.expensetrace.app.enums.AccountType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class AccountResponseDto {
    private Long id;
    private String name;
    private AccountType type;
    private BigDecimal currentBalance;
    private BigDecimal availableCredit;
    private BigDecimal creditLimit;
    private LocalDate billingStart;
    private LocalDate dueDate;
    private PaymentModeResponseDto paymentModesDto;
    private boolean isDefault;
}