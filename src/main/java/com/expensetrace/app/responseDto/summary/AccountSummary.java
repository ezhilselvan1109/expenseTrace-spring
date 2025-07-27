package com.expensetrace.app.responseDto.summary;
import com.expensetrace.app.responseDto.AccountResponseDto;
import lombok.Data;

@Data
public class AccountSummary {
    private AccountResponseDto accountResponseDto;
    private Integer amount;
}
