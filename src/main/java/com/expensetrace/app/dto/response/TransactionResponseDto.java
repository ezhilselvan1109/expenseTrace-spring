package com.expensetrace.app.dto.response;

import com.expensetrace.app.enums.TransactionType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class TransactionResponseDto {
    private UUID id;
    private TransactionType type;
    private int date;
    private int month;
    private int year;
    private BigDecimal amount;
    private UUID categoryId;
    private UUID accountId;
    private String description;
    private List<UUID> tagIds;
}

