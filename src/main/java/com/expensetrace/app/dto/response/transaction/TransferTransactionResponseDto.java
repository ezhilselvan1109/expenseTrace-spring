package com.expensetrace.app.dto.response.transaction;

import lombok.Data;

import java.util.UUID;

@Data
public class TransferTransactionResponseDto extends TransactionResponseDto {
    private UUID fromAccountId;

    private UUID toAccountId;
}
