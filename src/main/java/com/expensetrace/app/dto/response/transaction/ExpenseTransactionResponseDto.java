package com.expensetrace.app.dto.response.transaction;

import lombok.Data;

import java.util.UUID;
@Data
public class ExpenseTransactionResponseDto extends TransactionResponseDto {
    private UUID categoryId;

    private UUID accountId;

    private UUID paymentModeId;
}
