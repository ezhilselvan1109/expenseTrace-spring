package com.expensetrace.app.responseDto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WalletResponseDto extends AccountResponseDto{
    private BigDecimal currentBalance;
}
