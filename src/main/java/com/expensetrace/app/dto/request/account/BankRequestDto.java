package com.expensetrace.app.dto.request.account;

import com.expensetrace.app.dto.request.PaymentModeRequestDto;
import com.expensetrace.app.dto.request.account.AccountRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class BankRequestDto extends AccountRequestDto {

    @NotNull(message = "Current balance is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Current balance must be non-negative")
    private BigDecimal currentBalance;

    @Valid
    private List<@NotNull(message = "Payment mode cannot be null") PaymentModeRequestDto> linkedPaymentModes;
}
