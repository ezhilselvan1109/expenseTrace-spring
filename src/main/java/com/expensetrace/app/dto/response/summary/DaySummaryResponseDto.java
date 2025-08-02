package com.expensetrace.app.dto.response.summary;

import com.expensetrace.app.dto.response.transaction.TransactionResponseDto;
import lombok.Data;
import java.util.List;

@Data
public class DaySummaryResponseDto{
    private Integer spending;
    private Integer income;
    private List<TransactionResponseDto> transactions;
}