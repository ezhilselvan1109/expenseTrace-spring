package com.expensetrace.app.responseDto;

import com.expensetrace.app.enums.DebtsType;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class DebtResponseDto {
    private UUID id;
    private String personName;

    private LocalDate dueDate;

    private String additionalDetail;

    private DebtsType type;
}
