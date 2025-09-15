package com.expensetrace.app.dto.request.schedule;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class ScheduleTransferRequestDto extends ScheduleBaseRequestDto {
    @NotNull(message = "From Account ID is required")
    private UUID fromAccountId;

    @NotNull(message = "To Account ID is required")
    private UUID toAccountId;

    private UUID fromPaymentModeId;
    private UUID toPaymentModeId;
}

