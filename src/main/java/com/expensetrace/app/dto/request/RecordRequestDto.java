package com.expensetrace.app.dto.request;

import com.expensetrace.app.enums.RecordType;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class RecordRequestDto {
    private LocalDate date;

    private BigDecimal amount;

    private String description;

    private UUID accountId;

    private RecordType type;
}