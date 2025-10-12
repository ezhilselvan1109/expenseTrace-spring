package com.expensetrace.app.dto.response.transaction;

import com.expensetrace.app.category.dto.response.CategoryResponseDto;
import com.expensetrace.app.dto.response.account.AccountResponseDto;
import com.expensetrace.app.dto.response.account.PaymentModeResponseDto;
import com.expensetrace.app.dto.response.tag.TagResponseDto;
import lombok.Data;

import java.util.Set;

@Data
public class IncomeTransactionResponseDto extends TransactionResponseDto {
    private CategoryResponseDto category;

    private AccountResponseDto account;

    private PaymentModeResponseDto paymentMode;

    private Set<TagResponseDto> tags;
}
