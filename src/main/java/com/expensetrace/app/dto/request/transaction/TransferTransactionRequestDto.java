package com.expensetrace.app.dto.request.transaction;

import com.expensetrace.app.model.account.Account;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class TransferTransactionRequestDto extends TransactionRequestDto{
    @NotNull(message = "From Account ID is required")
    private UUID fromAccountId;
    @NotNull(message = "To Account ID is required")
    private UUID toAccountId;

    private List<UUID> tagIds;

    private List<String> tags;
}
