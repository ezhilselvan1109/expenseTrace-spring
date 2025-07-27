package com.expensetrace.app.responseDto.summary;

import lombok.Data;

@Data
public class MonthSummaryResponseDto {
    private Integer expense;
    private Integer income;
    private Integer day;
}
