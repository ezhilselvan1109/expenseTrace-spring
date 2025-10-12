package com.expensetrace.app.account.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CashResponseDto extends AccountResponseDto {
    private BigDecimal currentBalance;
}
