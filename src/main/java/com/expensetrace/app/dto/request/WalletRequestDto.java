package com.expensetrace.app.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WalletRequestDto extends AccountRequestDto{
    private BigDecimal currentBalance;
}
