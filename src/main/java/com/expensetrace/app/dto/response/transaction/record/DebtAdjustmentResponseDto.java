package com.expensetrace.app.dto.response.transaction.record;

import com.expensetrace.app.dto.response.debt.DebtResponseDto;
import com.expensetrace.app.dto.response.transaction.TransactionResponseDto;
import lombok.Data;

@Data
public class DebtAdjustmentResponseDto extends TransactionResponseDto {
    private DebtResponseDto debt;
}
