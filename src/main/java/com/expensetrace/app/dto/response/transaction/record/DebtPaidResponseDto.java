package com.expensetrace.app.dto.response.transaction.record;

import com.expensetrace.app.dto.response.DebtResponseDto;
import com.expensetrace.app.dto.response.account.AccountResponseDto;
import com.expensetrace.app.dto.response.account.PaymentModeResponseDto;
import com.expensetrace.app.dto.response.transaction.TransactionResponseDto;
import lombok.Data;


@Data
public class DebtPaidResponseDto extends TransactionResponseDto {

    private AccountResponseDto account;

    private PaymentModeResponseDto paymentMode;

    private DebtResponseDto debt;
}
