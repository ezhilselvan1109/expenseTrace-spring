package com.expensetrace.app.service.account.impl;

import com.expensetrace.app.dto.request.account.BankRequestDto;
import com.expensetrace.app.dto.response.account.BankResponseDto;
import com.expensetrace.app.enums.AccountType;
import com.expensetrace.app.exception.ResourceNotFoundException;
import com.expensetrace.app.model.User;
import com.expensetrace.app.model.account.Account;
import com.expensetrace.app.model.account.Bank;
import com.expensetrace.app.model.account.PaymentMode;
import com.expensetrace.app.repository.account.*;
import com.expensetrace.app.service.account.AccountServices;
import com.expensetrace.app.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BankService implements AccountServices<BankRequestDto, BankResponseDto> {

    private final BankRepository repository;
    private final ModelMapper modelMapper;
    private final SecurityUtil securityUtil;
    @Override
    public BankResponseDto create(BankRequestDto request) {
        UUID userId = securityUtil.getAuthenticatedUserId();
        Bank account = modelMapper.map(request, Bank.class);
        User user = new User();
        user.setId(userId);
        account.setUser(user);
        account.setType(AccountType.BANK);
        if (account.getType() == AccountType.BANK && request.getLinkedPaymentModes() != null) {
            List<PaymentMode> paymentModes = request.getLinkedPaymentModes().stream()
                    .map(pmDto -> {
                        PaymentMode pm = new PaymentMode();
                        pm.setName(pmDto.getName());
                        pm.setType(pmDto.getType());
                        pm.setAccount(account);
                        return pm;
                    })
                    .toList();

            account.setPaymentModes(paymentModes);
        }
        Account savedAccount = repository.save(account);
        return modelMapper.map(savedAccount, BankResponseDto.class);
    }

    @Override
    public BankResponseDto update(BankRequestDto request,UUID id) {
        Bank existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found!"));
        existing.setName(request.getName());

        Bank updated = repository.save(existing);
        return modelMapper.map(updated, BankResponseDto.class);
    }

    @Override
    public List<BankResponseDto> getAll() {
        UUID userId = securityUtil.getAuthenticatedUserId();
        return repository.findByUserIdAndType(userId, AccountType.BANK)
                .stream()
                .filter(account -> account instanceof Bank)
                .map(account -> modelMapper.map(account, BankResponseDto.class))
                .toList();
    }

}
