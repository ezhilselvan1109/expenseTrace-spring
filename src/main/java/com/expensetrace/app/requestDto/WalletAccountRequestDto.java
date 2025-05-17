package com.expensetrace.app.requestDto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WalletAccountRequestDto extends AccountRequestDto{
    private BigDecimal currentBalance;
}
