package com.expensetrace.app.dto.response.transaction.record;

import com.expensetrace.app.dto.response.debt.DebtResponseDto;
import com.expensetrace.app.account.dto.response.AccountResponseDto;
import com.expensetrace.app.account.dto.response.PaymentModeResponseDto;
import com.expensetrace.app.dto.response.transaction.TransactionResponseDto;
import lombok.Data;


@Data
public class DebtPaidResponseDto extends TransactionResponseDto {

    private AccountResponseDto account;

    private PaymentModeResponseDto paymentMode;

    private DebtResponseDto debt;
}
