package com.expensetrace.app.dto.response.schedule;

import com.expensetrace.app.dto.response.CategoryResponseDto;
import com.expensetrace.app.dto.response.account.AccountResponseDto;
import com.expensetrace.app.dto.response.account.PaymentModeResponseDto;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class ScheduleTransferResponseDto extends ScheduleBaseResponseDto {

    private AccountResponseDto fromAccount;

    private PaymentModeResponseDto fromPaymentMode;

    private AccountResponseDto toAccount;

    private PaymentModeResponseDto toPaymentMode;
}

