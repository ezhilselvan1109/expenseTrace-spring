package com.expensetrace.app.dto.response.schedule;

import com.expensetrace.app.dto.response.account.AccountResponseDto;
import com.expensetrace.app.dto.response.account.PaymentModeResponseDto;
import lombok.Data;

@Data
public class ScheduleTransferResponseDto extends ScheduleBaseResponseDto {

    private AccountResponseDto fromAccount;

    private PaymentModeResponseDto fromPaymentMode;

    private AccountResponseDto toAccount;

    private PaymentModeResponseDto toPaymentMode;
}

