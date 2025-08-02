package com.expensetrace.app.dto.request.transaction;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.UUID;
@Data
public class IncomeTransactionRequestDto extends TransactionRequestDto{
    @NotNull(message = "Category ID is required")
    private UUID categoryId;

    @NotNull(message = "Account ID is required")
    private UUID accountId;

    private List<UUID> tagIds;

    private List<String> tags;
}
