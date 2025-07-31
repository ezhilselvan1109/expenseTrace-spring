package com.expensetrace.app.dto.request;

import com.expensetrace.app.enums.DebtType;
import lombok.Data;
import java.time.LocalDate;

@Data
public class DebtRequestDto {
    private String personName;

    private LocalDate dueDate;

    private String additionalDetail;

    private DebtType type;

    private RecordRequestDto record;
}
