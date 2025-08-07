package com.expensetrace.app.dto.request.transaction.record;

import com.expensetrace.app.dto.request.transaction.TransactionRequestDto;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class AdjustmentRequestDto extends TransactionRequestDto {
    @NotNull(message = "Debt ID is required")
    private UUID debtId;
}
