package com.expensetrace.app.service.transaction.impl;

import com.expensetrace.app.dto.request.transaction.ExpenseTransactionRequestDto;
import com.expensetrace.app.dto.response.transaction.ExpenseTransactionResponseDto;
import com.expensetrace.app.enums.TransactionType;
import com.expensetrace.app.model.Category;
import com.expensetrace.app.model.Tag;
import com.expensetrace.app.model.User;
import com.expensetrace.app.model.account.Account;
import com.expensetrace.app.model.account.PaymentMode;
import com.expensetrace.app.model.transaction.ExpenseTransaction;
import com.expensetrace.app.repository.CategoryRepository;
import com.expensetrace.app.repository.TagRepository;
import com.expensetrace.app.repository.account.AccountRepository;
import com.expensetrace.app.repository.account.PaymentModeRepository;
import com.expensetrace.app.repository.transaction.ExpenseTransactionRepository;
import com.expensetrace.app.service.transaction.TransactionService;
import com.expensetrace.app.service.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service("expenseTransactionService")
@RequiredArgsConstructor
public class ExpenseTransactionServiceImpl implements TransactionService<ExpenseTransactionRequestDto, ExpenseTransactionResponseDto> {

    private final ExpenseTransactionRepository repository;
    private final ModelMapper mapper;
    private final UserService userService;
    private final TagRepository tagRepository;
    private final CategoryRepository categoryRepository;
    private final AccountRepository accountRepository;
    private final PaymentModeRepository paymentModeRepository;

    @Override
    @Transactional
    public ExpenseTransactionResponseDto create(ExpenseTransactionRequestDto request) {
        UUID userId = userService.getAuthenticatedUser().getId();
        User user = userService.getAuthenticatedUser();

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        ExpenseTransaction transaction = new ExpenseTransaction();
        transaction.setTxnDate(request.getTxnDate());
        transaction.setTxnTime(request.getTxnTime());
        transaction.setType(TransactionType.EXPENSE);
        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setCategory(category);
        transaction.setAccount(account);
        transaction.setUser(user);

        if (request.getPaymentModeId() != null) {
            PaymentMode paymentMode = paymentModeRepository.findById(request.getPaymentModeId())
                    .orElseThrow(() -> new RuntimeException("Payment mode not found"));
            transaction.setPaymentMode(paymentMode);
        }

        // persist the transaction first, so the subclass row exists
        ExpenseTransaction saved = repository.saveAndFlush(transaction);

        // now attach tags after the entity has an id and row in DB
        if (request.getTags() != null && !request.getTags().isEmpty()) {
            Set<Tag> tags = request.getTags().stream()
                    .map(tagName -> tagRepository.findByUserIdAndName(userId, tagName)
                            .orElseGet(() -> {
                                Tag newTag = new Tag();
                                newTag.setName(tagName);
                                newTag.setUser(user);
                                return tagRepository.save(newTag);
                            })
                    )
                    .collect(Collectors.toSet());

            saved.setTags(tags);
            saved = repository.save(saved);
        }

        return mapper.map(saved, ExpenseTransactionResponseDto.class);
    }

    @Override
    public ExpenseTransactionResponseDto update(UUID id, ExpenseTransactionRequestDto request) {
        UUID userId = userService.getAuthenticatedUser().getId();
        User user = userService.getAuthenticatedUser();

        ExpenseTransaction transaction = repository.findById(id)
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

        // update tags
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

        ExpenseTransaction saved = repository.save(transaction);
        return mapper.map(saved, ExpenseTransactionResponseDto.class);
    }
}