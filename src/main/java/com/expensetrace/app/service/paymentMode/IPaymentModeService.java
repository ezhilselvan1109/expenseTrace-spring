package com.expensetrace.app.service.paymentMode;

import com.expensetrace.app.requestDto.PaymentModeRequestDto;
import com.expensetrace.app.responseDto.PaymentModeResponseDto;

import java.util.List;
import java.util.UUID;

public interface IPaymentModeService {
    PaymentModeResponseDto getPaymentModeById(UUID id);
    List<PaymentModeResponseDto> getAllPaymentModes();
    List<PaymentModeResponseDto> getAllPaymentModesByAccount(UUID accountId);
    PaymentModeResponseDto addPaymentMode(UUID accountId,PaymentModeRequestDto user);
    PaymentModeResponseDto updatePaymentMode(PaymentModeRequestDto userRequest, UUID accountId);
    void deletePaymentModeById(UUID id);
}
