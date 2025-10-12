package com.expensetrace.app.account.service.impl;

import com.expensetrace.app.account.dto.request.WalletRequestDto;
import com.expensetrace.app.account.dto.response.WalletResponseDto;
import com.expensetrace.app.account.enums.AccountType;
import com.expensetrace.app.account.repository.WalletRepository;
import com.expensetrace.app.exception.ResourceNotFoundException;
import com.expensetrace.app.model.User;
import com.expensetrace.app.account.model.Account;
import com.expensetrace.app.account.model.Wallet;
import com.expensetrace.app.account.service.AccountServices;
import com.expensetrace.app.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletService implements AccountServices<WalletRequestDto, WalletResponseDto> {
    private final WalletRepository repository;
    private final ModelMapper modelMapper;
    private final SecurityUtil securityUtil;

    @Override
    public WalletResponseDto create(WalletRequestDto request) {
        UUID userId = securityUtil.getAuthenticatedUserId();
        Wallet account = modelMapper.map(request, Wallet.class);
        User user = new User();
        user.setId(userId);
        account.setUser(user);
        account.setType(AccountType.WALLET);
        Account savedAccount = repository.save(account);
        return modelMapper.map(savedAccount, WalletResponseDto.class);
    }

    @Override
    public List<WalletResponseDto> getAll() {
        UUID userId = securityUtil.getAuthenticatedUserId();
        return repository.findByUserIdAndType(userId, AccountType.WALLET)
                .stream()
                .filter(account -> account instanceof Wallet)
                .map(account -> modelMapper.map(account, WalletResponseDto.class))
                .toList();
    }

    @Override
    public WalletResponseDto update(WalletRequestDto request,UUID id) {
        Wallet existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found!"));
        existing.setName(request.getName());
        Account updated = repository.save(existing);
        return modelMapper.map(updated, WalletResponseDto.class);
    }
}
