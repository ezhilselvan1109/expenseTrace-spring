package com.expensetrace.app.dto.request.transaction;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;
@Data
public class AdjustmentTransactionRequestDto extends TransactionRequestDto{
    @NotNull(message = "Account ID is required")
    private UUID accountId;
}
