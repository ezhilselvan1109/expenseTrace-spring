package com.expensetrace.app.dto.response.transaction;

import lombok.Data;

import java.util.UUID;
@Data
public class IncomeTransactionResponseDto extends TransactionResponseDto {
    private UUID categoryId;

    private UUID accountId;

    private UUID paymentModeId;
}
