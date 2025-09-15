package com.expensetrace.app.service.transaction.impl;

import com.expensetrace.app.dto.request.transaction.AdjustmentTransactionRequestDto;
import com.expensetrace.app.dto.response.transaction.AdjustmentTransactionResponseDto;
import com.expensetrace.app.model.User;
import com.expensetrace.app.model.account.Account;
import com.expensetrace.app.model.transaction.AdjustmentTransaction;
import com.expensetrace.app.repository.account.AccountRepository;
import com.expensetrace.app.repository.transaction.AdjustmentTransactionRepository;
import com.expensetrace.app.service.transaction.TransactionService;
import com.expensetrace.app.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service("adjustmentTransactionService")
@RequiredArgsConstructor
public class AdjustmentTransactionServiceImpl  implements TransactionService<AdjustmentTransactionRequestDto, AdjustmentTransactionResponseDto> {
    private final AdjustmentTransactionRepository repository;
    private final ModelMapper mapper;
    private final UserService userService;
    private final AccountRepository accountRepository;
    @Override
    public AdjustmentTransactionResponseDto create(AdjustmentTransactionRequestDto request) {
        AdjustmentTransaction transaction = new AdjustmentTransaction();

        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        User user = userService.getAuthenticatedUser();

        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setAccount(account);
        transaction.setUser(user);

        AdjustmentTransaction saved = repository.save(transaction);

        return mapper.map(saved, AdjustmentTransactionResponseDto.class);
    }

    @Override
    public AdjustmentTransactionResponseDto update(UUID id, AdjustmentTransactionRequestDto request) {
        AdjustmentTransaction transaction = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());

        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));
        transaction.setAccount(account);

        AdjustmentTransaction saved = repository.save(transaction);
        return mapper.map(saved, AdjustmentTransactionResponseDto.class);
    }
}
