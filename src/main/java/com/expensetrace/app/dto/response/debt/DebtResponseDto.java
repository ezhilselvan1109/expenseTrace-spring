package com.expensetrace.app.dto.response.debt;

import com.expensetrace.app.enums.DebtType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class DebtResponseDto {
    private UUID id;
    private String personName;

    private LocalDate dueDate;

    private BigDecimal amount;

    private String additionalDetail;

    private DebtType type;
}
