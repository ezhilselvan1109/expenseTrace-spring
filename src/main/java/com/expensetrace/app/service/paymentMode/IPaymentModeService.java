package com.expensetrace.app.service.paymentMode;

import com.expensetrace.app.requestDto.PaymentModeRequestDto;
import com.expensetrace.app.responseDto.PaymentModeResponseDto;

import java.util.List;

public interface IPaymentModeService {
    PaymentModeResponseDto getPaymentModeById(Long id);
    List<PaymentModeResponseDto> getAllPaymentModes();
    List<PaymentModeResponseDto> getAllPaymentModesByAccount(Long accountId);
    PaymentModeResponseDto addPaymentMode(Long accountId,PaymentModeRequestDto user);
    PaymentModeResponseDto updatePaymentMode(PaymentModeRequestDto userRequest, Long accountId);
    void deletePaymentModeById(Long id);
}
