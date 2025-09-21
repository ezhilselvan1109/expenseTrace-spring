package com.expensetrace.app.dto.response.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class TransactionSummaryDto {
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
}

