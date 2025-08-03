package com.expensetrace.app.dto.response.transaction;

import com.expensetrace.app.dto.response.CategoryResponseDto;
import com.expensetrace.app.dto.response.account.AccountResponseDto;
import com.expensetrace.app.dto.response.account.PaymentModeResponseDto;
import lombok.Data;

@Data
public class IncomeTransactionResponseDto extends TransactionResponseDto {
    private CategoryResponseDto category;

    private AccountResponseDto account;

    private PaymentModeResponseDto paymentMode;
}
