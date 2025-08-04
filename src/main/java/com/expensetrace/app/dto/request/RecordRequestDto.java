package com.expensetrace.app.dto.request;

import com.expensetrace.app.enums.RecordType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class RecordRequestDto {

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than zero")
    private BigDecimal amount;

    @NotBlank(message = "Description must not be blank")
    private String description;

    @NotNull(message = "Account ID is required")
    private UUID accountId;

    private UUID paymentModeId;

    @NotNull(message = "Record type is required")
    private RecordType type;
}
