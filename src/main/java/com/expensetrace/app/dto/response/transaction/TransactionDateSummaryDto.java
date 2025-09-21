package com.expensetrace.app.dto.response.transaction;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;

@Data
public class TransactionDateSummaryDto {
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private Page<TransactionResponseDto> transactions;
}

