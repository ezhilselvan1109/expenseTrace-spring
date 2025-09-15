package com.expensetrace.app.dto.request.schedule;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class ScheduleExpenseRequestDto extends ScheduleBaseRequestDto {
    @NotNull(message = "Category ID is required")
    private UUID categoryId;

    @NotNull(message = "Account ID is required")
    private UUID accountId;

    private UUID paymentModeId;
}
