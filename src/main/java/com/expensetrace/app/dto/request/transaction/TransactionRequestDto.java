package com.expensetrace.app.dto.request.transaction;

import com.expensetrace.app.enums.TransactionType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionRequestDto {

    @NotNull(message = "Transaction type is required")
    private TransactionType type;

    @Min(value = 1, message = "Date must be between 1 and 31")
    @Max(value = 31, message = "Date must be between 1 and 31")
    private int date;

    @Min(value = 1, message = "Month must be between 1 and 12")
    @Max(value = 12, message = "Month must be between 1 and 12")
    private int month;

    @Min(value = 2000, message = "Year must be a valid year")
    private int year;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than zero")
    private BigDecimal amount;

    @Size(max = 255, message = "Description can't exceed 255 characters")
    private String description;
}
