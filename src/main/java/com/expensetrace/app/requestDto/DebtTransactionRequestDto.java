package com.expensetrace.app.requestDto;

import com.expensetrace.app.enums.DebtTransactionType;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class DebtTransactionRequestDto {
    private LocalDate date;

    private LocalTime time;

    private BigDecimal amount;

    private String description;

    private DebtTransactionType type;
}