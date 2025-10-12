package com.expensetrace.app.service.schedule.impl;

import com.expensetrace.app.dto.request.schedule.ScheduleExpenseRequestDto;
import com.expensetrace.app.dto.response.schedule.ScheduleExpenseResponseDto;
import com.expensetrace.app.enums.schedule.ExecutionStatus;
import com.expensetrace.app.enums.schedule.ScheduleType;
import com.expensetrace.app.category.model.Category;
import com.expensetrace.app.model.Tag;
import com.expensetrace.app.model.User;
import com.expensetrace.app.model.account.Account;
import com.expensetrace.app.model.account.PaymentMode;
import com.expensetrace.app.model.schedule.ScheduledExpenseTransaction;
import com.expensetrace.app.model.schedule.ScheduledTransaction;
import com.expensetrace.app.category.repository.CategoryRepository;
import com.expensetrace.app.repository.TagRepository;
import com.expensetrace.app.repository.account.AccountRepository;
import com.expensetrace.app.repository.account.PaymentModeRepository;
import com.expensetrace.app.repository.schedule.ScheduledExpenseTransactionRepository;
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
public class ScheduledExpenseService implements ScheduledTransactionService<ScheduleExpenseRequestDto, ScheduleExpenseResponseDto> {
    private final ScheduledExpenseTransactionRepository repository;
    private final ModelMapper mapper;
    private final UserService userService;
    private final TagRepository tagRepository;
    private final CategoryRepository categoryRepository;
    private final AccountRepository accountRepository;
    private final PaymentModeRepository paymentModeRepository;

    @Override
    public ScheduleExpenseResponseDto create(ScheduleExpenseRequestDto request) {
        UUID userId = userService.getAuthenticatedUser().getId();
        User user = userService.getAuthenticatedUser();

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        ScheduledExpenseTransaction transaction = new ScheduledExpenseTransaction();
        transaction.setType(ScheduleType.EXPENSE);
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

        transaction.setCategory(category);
        transaction.setAccount(account);
        transaction.setUser(user);

        if (request.getPaymentModeId() != null) {
            PaymentMode paymentMode = paymentModeRepository.findById(request.getPaymentModeId())
                    .orElseThrow(() -> new RuntimeException("Payment mode not found"));
            transaction.setPaymentMode(paymentMode);
        }

        ScheduledExpenseTransaction saved = repository.saveAndFlush(transaction);

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

        return mapper.map(saved, ScheduleExpenseResponseDto.class);
    }

    @Override
    public ScheduleExpenseResponseDto update(UUID id,ScheduleExpenseRequestDto request){
        return null;
    }
    @Override
    public ScheduleExpenseResponseDto toResponse(ScheduledTransaction transaction) {
        return mapper.map(transaction, ScheduleExpenseResponseDto.class);
    }
}


