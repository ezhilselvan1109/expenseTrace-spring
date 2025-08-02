package com.expensetrace.app.dto.response.transaction;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.UUID;
@Data
public class ExpenseTransactionResponseDto extends TransactionResponseDto {
    private UUID categoryId;

    private UUID accountId;

    private List<UUID> tagIds;

    private List<String> tags;
}
