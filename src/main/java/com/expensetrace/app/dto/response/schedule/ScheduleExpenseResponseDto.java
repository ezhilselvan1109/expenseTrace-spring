package com.expensetrace.app.dto.response.schedule;

import com.expensetrace.app.dto.response.CategoryResponseDto;
import com.expensetrace.app.dto.response.account.AccountResponseDto;
import com.expensetrace.app.dto.response.account.PaymentModeResponseDto;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class ScheduleExpenseResponseDto extends ScheduleBaseResponseDto {
    private CategoryResponseDto category;

    private AccountResponseDto account;

    private PaymentModeResponseDto paymentMode;
}
