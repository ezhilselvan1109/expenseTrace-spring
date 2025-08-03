package com.expensetrace.app.dto.response.transaction;

import com.expensetrace.app.dto.response.account.AccountResponseDto;
import com.expensetrace.app.dto.response.account.PaymentModeResponseDto;
import com.expensetrace.app.dto.response.tag.TagResponseDto;
import lombok.Data;

import java.util.Set;

@Data
public class TransferTransactionResponseDto extends TransactionResponseDto {
    private AccountResponseDto fromAccount;

    private AccountResponseDto toAccount;

    private PaymentModeResponseDto fromPaymentMode;

    private PaymentModeResponseDto toPaymentMode;

    private Set<TagResponseDto> tags;
}
