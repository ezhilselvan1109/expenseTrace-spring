package com.expensetrace.app.responseDto;

import com.expensetrace.app.enums.DebtTransactionType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
public class DebtTransactionResponseDto {
    private UUID id;
    private LocalDate date;

    private LocalTime time;

    private BigDecimal amount;

    private String description;

    private DebtTransactionType type;
}