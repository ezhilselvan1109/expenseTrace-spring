package com.expensetrace.app.dto.response;

import com.expensetrace.app.enums.RecordType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
public class RecordResponseDto {
    private UUID id;
    private LocalDate date;

    private LocalTime time;

    private BigDecimal amount;

    private UUID paymentModeId;

    private String description;

    private RecordType type;
}