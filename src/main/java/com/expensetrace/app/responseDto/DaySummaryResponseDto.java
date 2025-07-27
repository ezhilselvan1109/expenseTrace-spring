package com.expensetrace.app.responseDto;

import lombok.Data;
import java.util.List;

@Data
public class DaySummaryResponseDto{
    private Integer spending;
    private Integer income;
    private List<TransactionResponseDto> transactions;
}