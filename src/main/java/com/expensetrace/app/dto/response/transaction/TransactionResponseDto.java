package com.expensetrace.app.dto.response.transaction;

import com.expensetrace.app.enums.TransactionType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class TransactionResponseDto {
    private UUID id;

    private TransactionType type;

    private int date;

    private int month;

    private int year;

    private BigDecimal amount;

    private String description;
}
