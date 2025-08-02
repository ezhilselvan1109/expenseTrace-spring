package com.expensetrace.app.dto.response.transaction;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class TransferTransactionResponseDto extends TransactionResponseDto {
    private UUID fromAccountId;

    private UUID toAccountId;

    private List<UUID> tagIds;

    private List<String> tags;
}
