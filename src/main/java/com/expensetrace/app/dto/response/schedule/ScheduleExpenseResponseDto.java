package com.expensetrace.app.dto.response.schedule;

import com.expensetrace.app.category.dto.response.CategoryResponseDto;
import com.expensetrace.app.account.dto.response.AccountResponseDto;
import com.expensetrace.app.account.dto.response.PaymentModeResponseDto;
import lombok.Data;

@Data
public class ScheduleExpenseResponseDto extends ScheduleBaseResponseDto {
    private CategoryResponseDto category;

    private AccountResponseDto account;

    private PaymentModeResponseDto paymentMode;
}
