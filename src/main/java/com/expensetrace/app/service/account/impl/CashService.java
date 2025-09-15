package com.expensetrace.app.service.account.impl;

import com.expensetrace.app.dto.request.account.CashRequestDto;
import com.expensetrace.app.dto.response.account.CashResponseDto;
import com.expensetrace.app.enums.AccountType;
import com.expensetrace.app.model.account.Cash;
import com.expensetrace.app.repository.account.CashRepository;
import com.expensetrace.app.service.account.AccountServices;
import com.expensetrace.app.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CashService implements AccountServices<CashRequestDto, CashResponseDto> {
    private final CashRepository repository;
    private final ModelMapper modelMapper;
    private final SecurityUtil securityUtil;

    @Override
    public CashResponseDto create(CashRequestDto request) {
        return null;
    }

    @Override
    public List<CashResponseDto> getAll() {
        UUID userId = securityUtil.getAuthenticatedUserId();
        return repository.findByUserIdAndType(userId, AccountType.CASH)
                .stream()
                .filter(account -> account instanceof Cash)
                .map(account -> modelMapper.map(account, CashResponseDto.class))
                .toList();
    }

    @Override
    public CashResponseDto update(CashRequestDto request, UUID id) {
        return null;
    }
}
