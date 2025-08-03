package com.expensetrace.app.dto.response.transaction;

import com.expensetrace.app.dto.response.account.AccountResponseDto;
import lombok.Data;

@Data
public class AdjustmentTransactionResponseDto extends TransactionResponseDto {
    private AccountResponseDto account;
}
