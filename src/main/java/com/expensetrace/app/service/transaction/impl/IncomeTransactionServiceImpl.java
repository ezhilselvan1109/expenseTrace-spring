package com.expensetrace.app.service.transaction.impl;

import com.expensetrace.app.dto.request.transaction.IncomeTransactionRequestDto;
import com.expensetrace.app.dto.response.transaction.IncomeTransactionResponseDto;
import com.expensetrace.app.category.model.Category;
import com.expensetrace.app.model.Tag;
import com.expensetrace.app.model.User;
import com.expensetrace.app.account.model.Account;
import com.expensetrace.app.account.model.PaymentMode;
import com.expensetrace.app.model.transaction.IncomeTransaction;
import com.expensetrace.app.category.repository.CategoryRepository;
import com.expensetrace.app.repository.TagRepository;
import com.expensetrace.app.account.repository.AccountRepository;
import com.expensetrace.app.account.repository.PaymentModeRepository;
import com.expensetrace.app.repository.transaction.IncomeTransactionRepository;
import com.expensetrace.app.service.transaction.TransactionService;
import com.expensetrace.app.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service("incomeTransactionService")
@RequiredArgsConstructor
public class IncomeTransactionServiceImpl implements TransactionService<IncomeTransactionRequestDto, IncomeTransactionResponseDto> {

    private final IncomeTransactionRepository repository;
    private final ModelMapper mapper;
    private final UserService userService;
    private final TagRepository tagRepository;
    private final CategoryRepository categoryRepository;
    private final AccountRepository accountRepository;
    private final PaymentModeRepository paymentModeRepository;
    @Override
    public IncomeTransactionResponseDto create(IncomeTransactionRequestDto request) {
        IncomeTransaction transaction = new IncomeTransaction();
        UUID userId = userService.getAuthenticatedUser().getId();
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));
        if (request.getPaymentModeId() != null) {
            PaymentMode paymentMode = paymentModeRepository.findById(request.getPaymentModeId())
                    .orElseThrow(() -> new RuntimeException("Payment mode not found"));
            transaction.setPaymentMode(paymentMode);
        }
        User user = userService.getAuthenticatedUser();
        transaction.setTxnDate(request.getTxnDate());
        transaction.setTxnTime(request.getTxnTime());
        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setCategory(category);
        transaction.setAccount(account);
        transaction.setUser(user);

        if (request.getTags() != null && !request.getTags().isEmpty()) {
            Set<Tag> tags = request.getTags().stream()
                    .map(tagName -> tagRepository.findByUserIdAndName(userId,tagName)
                            .orElseGet(() -> {
                                Tag newTag = new Tag();
                                newTag.setName(tagName);
                                newTag.setUser(user);
                                return tagRepository.save(newTag);
                            })
                    )
                    .collect(Collectors.toSet());

            transaction.setTags(tags);
        }

        IncomeTransaction saved = repository.save(transaction);

        return mapper.map(saved, IncomeTransactionResponseDto.class);
    }

    @Override
    public IncomeTransactionResponseDto update(UUID id, IncomeTransactionRequestDto request) {
        UUID userId = userService.getAuthenticatedUser().getId();
        User user = userService.getAuthenticatedUser();

        IncomeTransaction transaction = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        transaction.setCategory(category);

        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));
        transaction.setAccount(account);

        if (request.getPaymentModeId() != null) {
            PaymentMode paymentMode = paymentModeRepository.findById(request.getPaymentModeId())
                    .orElseThrow(() -> new RuntimeException("Payment mode not found"));
            transaction.setPaymentMode(paymentMode);
        } else {
            transaction.setPaymentMode(null);
        }

        if (request.getTags() != null) {
            Set<Tag> updatedTags = request.getTags().stream()
                    .map(tagName -> tagRepository.findByUserIdAndName(userId, tagName)
                            .orElseGet(() -> {
                                Tag newTag = new Tag();
                                newTag.setName(tagName);
                                newTag.setUser(user);
                                return tagRepository.save(newTag);
                            })
                    )
                    .collect(Collectors.toSet());
            transaction.setTags(updatedTags);
        } else {
            transaction.getTags().clear();
        }

        IncomeTransaction saved = repository.save(transaction);
        return mapper.map(saved, IncomeTransactionResponseDto.class);
    }
}