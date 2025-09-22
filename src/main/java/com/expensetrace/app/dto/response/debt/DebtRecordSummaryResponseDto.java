package com.expensetrace.app.dto.response.debt;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
public class DebtRecordSummaryResponseDto {
    private UUID bebtId;
    private BigDecimal totalPaid;
    private BigDecimal totalReceived;
}
