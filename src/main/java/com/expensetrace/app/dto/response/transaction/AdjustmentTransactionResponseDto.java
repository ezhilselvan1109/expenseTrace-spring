package com.expensetrace.app.dto.response.transaction;

import com.expensetrace.app.dto.response.account.AccountResponseDto;
import com.expensetrace.app.dto.response.account.PaymentModeResponseDto;
import lombok.Data;

@Data
public class AdjustmentTransactionResponseDto extends TransactionResponseDto {
    private AccountResponseDto account;
}
