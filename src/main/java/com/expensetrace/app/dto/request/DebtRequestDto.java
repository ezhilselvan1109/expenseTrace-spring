package com.expensetrace.app.dto.request;

import com.expensetrace.app.enums.DebtType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DebtRequestDto {

    @NotBlank(message = "Person name is required")
    private String personName;

    @NotNull(message = "Due date is required")
    @FutureOrPresent(message = "Due date must be today or in the future")
    private LocalDate dueDate;

    private String additionalDetail;

    @NotNull(message = "Debt type is required")
    private DebtType type;

    @NotNull(message = "Record is required")
    @Valid
    private RecordRequestDto record;
}
