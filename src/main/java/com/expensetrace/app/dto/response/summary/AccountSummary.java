package com.expensetrace.app.dto.response.summary;
import com.expensetrace.app.dto.response.account.AccountResponseDto;
import lombok.Data;

@Data
public class AccountSummary {
    private AccountResponseDto accountResponseDto;
    private Integer amount;
}
