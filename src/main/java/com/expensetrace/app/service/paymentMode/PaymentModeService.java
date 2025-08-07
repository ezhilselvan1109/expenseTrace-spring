package com.expensetrace.app.service.paymentMode;

import com.expensetrace.app.exception.AlreadyExistsException;
import com.expensetrace.app.exception.ResourceNotFoundException;
import com.expensetrace.app.model.account.Bank;
import com.expensetrace.app.model.account.PaymentMode;
import com.expensetrace.app.model.account.Account;
import com.expensetrace.app.model.transaction.Transaction;
import com.expensetrace.app.repository.account.PaymentModeRepository;
import com.expensetrace.app.dto.request.account.PaymentModeRequestDto;
import com.expensetrace.app.dto.response.account.PaymentModeResponseDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentModeService implements IPaymentModeService {
    private final PaymentModeRepository paymentModeRepository;
    private final ModelMapper modelMapper;
    @Override
    public PaymentModeResponseDto getPaymentModeById(UUID id) {
        PaymentMode paymentMode = paymentModeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PaymentMode not found!"));
        return modelMapper.map(paymentMode, PaymentModeResponseDto.class);
    }

    @Override
    public List<PaymentModeResponseDto> getAllPaymentModes() {
        return paymentModeRepository.findAll()
                .stream()
                .map(paymentMode -> modelMapper.map(paymentMode, PaymentModeResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentModeResponseDto> getAllPaymentModesByAccount(UUID accountId) {
        return paymentModeRepository.findByAccountId(accountId)
                .stream()
                .map(paymentMode -> modelMapper.map(paymentMode, PaymentModeResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public PaymentModeResponseDto addPaymentMode(UUID accountId,PaymentModeRequestDto paymentModeRequestDto) {
        if (paymentModeRepository.existsByName(paymentModeRequestDto.getName())) {
            throw new AlreadyExistsException(paymentModeRequestDto.getName() + " already exists");
        }

        PaymentMode paymentMode = modelMapper.map(paymentModeRequestDto, PaymentMode.class);

        // Account reference
        Account account = new Bank();
        account.setId(accountId); // Only setting ID is enough for association
        paymentMode.setAccount(account);

        PaymentMode savedPaymentMode = paymentModeRepository.save(paymentMode);
        return modelMapper.map(savedPaymentMode, PaymentModeResponseDto.class);
    }


    @Override
    public PaymentModeResponseDto updatePaymentMode(PaymentModeRequestDto paymentModeRequestDto, UUID id) {
        PaymentMode existingPaymentMode = paymentModeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PaymentMode not found!"));

        existingPaymentMode.setName(paymentModeRequestDto.getName());
        existingPaymentMode.setType(paymentModeRequestDto.getType());
        PaymentMode updatedPaymentMode = paymentModeRepository.save(existingPaymentMode);
        return modelMapper.map(updatedPaymentMode, PaymentModeResponseDto.class);
    }



    @Override
    public void deletePaymentModeById(UUID id) {
        paymentModeRepository.findById(id)
                .ifPresentOrElse(paymentModeRepository::delete, () -> {
                    throw new ResourceNotFoundException("PaymentMode not found!");
                });
    }

    public PaymentMode getPaymentMode(UUID payId) {
        return paymentModeRepository.findById(payId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment-mode not found"));
    }
}
