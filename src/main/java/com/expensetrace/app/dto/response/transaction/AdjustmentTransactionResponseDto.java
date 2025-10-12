package com.expensetrace.app.dto.response.transaction;

import com.expensetrace.app.account.dto.response.AccountResponseDto;
import lombok.Data;

@Data
public class AdjustmentTransactionResponseDto extends TransactionResponseDto {
    private AccountResponseDto account;
}
