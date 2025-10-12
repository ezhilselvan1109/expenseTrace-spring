package com.expensetrace.app.dto.response.schedule;

import com.expensetrace.app.category.dto.response.CategoryResponseDto;
import com.expensetrace.app.dto.response.account.AccountResponseDto;
import com.expensetrace.app.dto.response.account.PaymentModeResponseDto;
import lombok.Data;

@Data
public class ScheduleIncomeResponseDto extends ScheduleBaseResponseDto {
    private CategoryResponseDto category;

    private AccountResponseDto account;

    private PaymentModeResponseDto paymentMode;
}
