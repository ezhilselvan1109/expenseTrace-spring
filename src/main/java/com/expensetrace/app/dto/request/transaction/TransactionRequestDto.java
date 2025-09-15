package com.expensetrace.app.dto.request.transaction;

import com.expensetrace.app.enums.TransactionType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class TransactionRequestDto {

    @NotNull(message = "Transaction type is required")
    private TransactionType type;

    @NotNull(message = "Transaction date is required")
    private LocalDate txnDate;

    @NotNull(message = "Transaction time is required")
    private LocalTime txnTime;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than zero")
    private BigDecimal amount;

    @Size(max = 255, message = "Description can't exceed 255 characters")
    private String description;
}
