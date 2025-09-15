package com.expensetrace.app.dto.request.transaction;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.UUID;
@Data
public class ExpenseTransactionRequestDto extends TransactionRequestDto{
    @NotNull(message = "Category ID is required")
    private UUID categoryId;

    @NotNull(message = "Account ID is required")
    private UUID accountId;

    private UUID paymentModeId;

    private List<String> tags;
}
