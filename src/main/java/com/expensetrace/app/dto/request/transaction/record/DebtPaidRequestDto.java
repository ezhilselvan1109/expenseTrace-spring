package com.expensetrace.app.dto.request.transaction.record;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.UUID;

@Data
public class DebtPaidRequestDto  extends DebtRecordRequestDto {

    @NotNull(message = "Account ID is required")
    private UUID accountId;

    private UUID paymentModeId;
}
