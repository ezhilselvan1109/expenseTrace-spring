package com.expensetrace.app.dto.response.account;

import com.expensetrace.app.dto.response.account.AccountResponseDto;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CashResponseDto extends AccountResponseDto {
    private BigDecimal currentBalance;
}
