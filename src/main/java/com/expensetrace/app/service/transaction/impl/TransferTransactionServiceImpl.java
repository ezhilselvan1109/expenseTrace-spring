package com.expensetrace.app.service.transaction.impl;

import com.expensetrace.app.dto.request.transaction.TransferTransactionRequestDto;
import com.expensetrace.app.dto.response.transaction.TransferTransactionResponseDto;
import com.expensetrace.app.model.Tag;
import com.expensetrace.app.model.User;
import com.expensetrace.app.model.account.Account;
import com.expensetrace.app.model.account.PaymentMode;
import com.expensetrace.app.model.transaction.TransferTransaction;
import com.expensetrace.app.repository.TagRepository;
import com.expensetrace.app.repository.account.AccountRepository;
import com.expensetrace.app.repository.account.PaymentModeRepository;
import com.expensetrace.app.repository.transaction.TransferTransactionRepository;
import com.expensetrace.app.service.transaction.TransactionService;
import com.expensetrace.app.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service("transferTransactionService")
@RequiredArgsConstructor
public class TransferTransactionServiceImpl implements TransactionService<TransferTransactionRequestDto, TransferTransactionResponseDto> {
    private final TransferTransactionRepository repository;
    private final ModelMapper mapper;
    private final UserService userService;
    private final TagRepository tagRepository;
    private final AccountRepository accountRepository;
    private final PaymentModeRepository paymentModeRepository;
    @Override
    public TransferTransactionResponseDto create(TransferTransactionRequestDto request) {
        TransferTransaction transaction = new TransferTransaction();
        UUID userId = userService.getAuthenticatedUser().getId();
        Account fromAccount = accountRepository.findById(request.getFromAccountId())
                .orElseThrow(() -> new RuntimeException("From Account not found"));
        Account toAccount = accountRepository.findById(request.getToAccountId())
                .orElseThrow(() -> new RuntimeException("To Account not found"));

        if (request.getFromPaymentModeId() != null) {
            PaymentMode paymentMode = paymentModeRepository.findById(request.getFromPaymentModeId())
                    .orElseThrow(() -> new RuntimeException("From Payment mode not found"));
            transaction.setFromPaymentMode(paymentMode);
        }

        if (request.getToPaymentModeId() != null) {
            PaymentMode paymentMode = paymentModeRepository.findById(request.getToPaymentModeId())
                    .orElseThrow(() -> new RuntimeException("To Payment mode not found"));
            transaction.setToPaymentMode(paymentMode);
        }

        User user = userService.getAuthenticatedUser();
        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(toAccount);
        transaction.setUser(user);
        transaction.setTxnDate(request.getTxnDate());
        transaction.setTxnTime(request.getTxnTime());
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

        TransferTransaction saved = repository.save(transaction);

        return mapper.map(saved, TransferTransactionResponseDto.class);
    }

    @Override
    public TransferTransactionResponseDto update(UUID id, TransferTransactionRequestDto request) {
        UUID userId = userService.getAuthenticatedUser().getId();
        User user = userService.getAuthenticatedUser();

        TransferTransaction transaction = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());

        Account fromAccount = accountRepository.findById(request.getFromAccountId())
                .orElseThrow(() -> new RuntimeException("From Account not found"));
        Account toAccount = accountRepository.findById(request.getToAccountId())
                .orElseThrow(() -> new RuntimeException("To Account not found"));

        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(toAccount);

        if (request.getFromPaymentModeId() != null) {
            PaymentMode paymentMode = paymentModeRepository.findById(request.getFromPaymentModeId())
                    .orElseThrow(() -> new RuntimeException("From Payment mode not found"));
            transaction.setFromPaymentMode(paymentMode);
        }

        if (request.getToPaymentModeId() != null) {
            PaymentMode paymentMode = paymentModeRepository.findById(request.getToPaymentModeId())
                    .orElseThrow(() -> new RuntimeException("To Payment mode not found"));
            transaction.setToPaymentMode(paymentMode);
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

        TransferTransaction saved = repository.save(transaction);
        return mapper.map(saved, TransferTransactionResponseDto.class);
    }
}
