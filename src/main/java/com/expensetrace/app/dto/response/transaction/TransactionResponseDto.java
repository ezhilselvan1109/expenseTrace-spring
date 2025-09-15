package com.expensetrace.app.dto.response.transaction;

import com.expensetrace.app.enums.TransactionType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
public class TransactionResponseDto {

    private UUID id;

    private LocalDate txnDate;

    private LocalTime txnTime;

    private BigDecimal amount;

    private TransactionType type;

    private String description;
}
