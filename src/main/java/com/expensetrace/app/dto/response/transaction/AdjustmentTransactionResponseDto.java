package com.expensetrace.app.dto.response.transaction;

import lombok.Data;

import java.util.UUID;
@Data
public class AdjustmentTransactionResponseDto extends TransactionResponseDto {
    private UUID accountId;
}
