package com.expensetrace.app.account.service.impl;

import com.expensetrace.app.account.dto.request.CreditCardRequestDto;
import com.expensetrace.app.account.dto.response.CreditCardResponseDto;
import com.expensetrace.app.account.enums.AccountType;
import com.expensetrace.app.account.repository.CreditCardRepository;
import com.expensetrace.app.exception.ResourceNotFoundException;
import com.expensetrace.app.model.User;
import com.expensetrace.app.account.model.Account;
import com.expensetrace.app.account.model.CreditCard;
import com.expensetrace.app.account.service.AccountServices;
import com.expensetrace.app.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreditCardService implements AccountServices<CreditCardRequestDto, CreditCardResponseDto> {
    private final CreditCardRepository repository;
    private final ModelMapper modelMapper;
    private final SecurityUtil securityUtil;

    @Override
    public CreditCardResponseDto create(CreditCardRequestDto request) {
        UUID userId = securityUtil.getAuthenticatedUserId();
        CreditCard account = modelMapper.map(request, CreditCard.class);
        User user = new User();
        user.setId(userId);
        account.setUser(user);
        account.setType(AccountType.CREDIT_CARD);
        Account savedAccount = repository.save(account);
        return modelMapper.map(savedAccount, CreditCardResponseDto.class);
    }

    @Override
    public List<CreditCardResponseDto> getAll() {
        UUID userId = securityUtil.getAuthenticatedUserId();
        return repository.findByUserIdAndType(userId, AccountType.CREDIT_CARD)
                .stream()
                .filter(account -> account instanceof CreditCard)
                .map(account -> modelMapper.map(account, CreditCardResponseDto.class))
                .toList();
    }

    @Override
    public CreditCardResponseDto update(CreditCardRequestDto request,UUID id) {
        CreditCard existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found!"));
        existing.setName(request.getName());
        existing.setPaymentDueDate(request.getPaymentDueDate());
        existing.setCurrentAvailableLimit(request.getCurrentAvailableLimit());
        existing.setTotalCreditLimit(request.getTotalCreditLimit());
        existing.setBillingCycleStartDate(request.getBillingCycleStartDate());
        Account updated = repository.save(existing);
        return modelMapper.map(updated, CreditCardResponseDto.class);
    }
}
