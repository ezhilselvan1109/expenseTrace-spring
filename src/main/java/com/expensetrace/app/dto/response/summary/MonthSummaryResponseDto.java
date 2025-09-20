package com.expensetrace.app.dto.response.summary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthSummaryResponseDto {
    private BigDecimal expense;
    private BigDecimal income;
    private Integer day;
}
