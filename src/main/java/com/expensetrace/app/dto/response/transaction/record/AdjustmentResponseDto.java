package com.expensetrace.app.dto.response.transaction.record;

import com.expensetrace.app.dto.response.DebtResponseDto;
import com.expensetrace.app.dto.response.transaction.TransactionResponseDto;
import lombok.Data;

@Data
public class AdjustmentResponseDto extends TransactionResponseDto {
    private DebtResponseDto debt;
}
