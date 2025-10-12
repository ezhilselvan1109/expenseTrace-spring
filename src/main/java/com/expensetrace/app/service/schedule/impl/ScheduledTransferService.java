package com.expensetrace.app.service.schedule.impl;

import com.expensetrace.app.dto.request.schedule.ScheduleTransferRequestDto;
import com.expensetrace.app.dto.response.schedule.ScheduleTransferResponseDto;
import com.expensetrace.app.enums.schedule.ExecutionStatus;
import com.expensetrace.app.enums.schedule.ScheduleType;
import com.expensetrace.app.model.Tag;
import com.expensetrace.app.model.User;
import com.expensetrace.app.account.model.Account;
import com.expensetrace.app.account.model.PaymentMode;
import com.expensetrace.app.model.schedule.ScheduledTransaction;
import com.expensetrace.app.model.schedule.ScheduledTransferTransaction;
import com.expensetrace.app.repository.TagRepository;
import com.expensetrace.app.account.repository.AccountRepository;
import com.expensetrace.app.account.repository.PaymentModeRepository;
import com.expensetrace.app.repository.schedule.ScheduledTransferTransactionRepository;
import com.expensetrace.app.service.schedule.ScheduledTransactionService;
import com.expensetrace.app.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduledTransferService implements ScheduledTransactionService<ScheduleTransferRequestDto, ScheduleTransferResponseDto> {
    private final ScheduledTransferTransactionRepository repository;
    private final ModelMapper mapper;
    private final UserService userService;
    private final TagRepository tagRepository;
    private final AccountRepository accountRepository;
    private final PaymentModeRepository paymentModeRepository;

    @Override
    public ScheduleTransferResponseDto create(ScheduleTransferRequestDto request) {
        UUID userId = userService.getAuthenticatedUser().getId();
        User user = userService.getAuthenticatedUser();

        Account fromAccount = accountRepository.findById(request.getFromAccountId())
                .orElseThrow(() -> new RuntimeException("From Account not found"));

        Account toAccount = accountRepository.findById(request.getToAccountId())
                .orElseThrow(() -> new RuntimeException("To Account not found"));

        ScheduledTransferTransaction transaction = new ScheduledTransferTransaction();
        transaction.setType(ScheduleType.TRANSFER);
        transaction.setStartDate(request.getStartDate());
        transaction.setStartTime(request.getStartTime());
        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setFrequencyType(request.getFrequencyType());
        transaction.setFrequencyInterval(request.getFrequencyInterval());
        transaction.setEndType(request.getEndType());
        transaction.setOccurrence(request.getOccurrence());
        transaction.setReminderDays(request.getRemainderDays());
        transaction.setStatus(ExecutionStatus.UPCOMING);

        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(toAccount);
        transaction.setUser(user);

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

        ScheduledTransferTransaction saved = repository.saveAndFlush(transaction);

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

        return mapper.map(saved, ScheduleTransferResponseDto.class);
    }

    @Override
    public ScheduleTransferResponseDto update(UUID id,ScheduleTransferRequestDto request) {
        return null;
    }


    @Override
    public ScheduleTransferResponseDto toResponse(ScheduledTransaction transaction) {
        return mapper.map(transaction, ScheduleTransferResponseDto.class);
    }
}

