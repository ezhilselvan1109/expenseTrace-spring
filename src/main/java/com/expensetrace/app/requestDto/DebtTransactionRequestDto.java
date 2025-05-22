package com.expensetrace.app.requestDto;

import com.expensetrace.app.enums.DebtTransactionType;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
public class DebtTransactionRequestDto {
    private LocalDate date;

    private BigDecimal amount;

    private String description;

    private UUID accountId;

    private DebtTransactionType type;
}