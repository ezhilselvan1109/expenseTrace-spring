package com.expensetrace.app.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class DebtSummaryResponseDto {
    private BigDecimal totalPayable;
    private BigDecimal totalReceivable;
}
