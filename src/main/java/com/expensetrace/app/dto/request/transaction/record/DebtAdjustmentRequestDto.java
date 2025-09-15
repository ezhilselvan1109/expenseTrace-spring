package com.expensetrace.app.dto.request.transaction.record;

import com.expensetrace.app.dto.request.transaction.TransactionRequestDto;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class DebtAdjustmentRequestDto extends TransactionRequestDto {
}
