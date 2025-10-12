package com.expensetrace.app.account.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WalletRequestDto extends AccountRequestDto {

    @NotNull(message = "Current balance is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Current balance must be zero or positive")
    private BigDecimal currentBalance;
}
