package com.expensetrace.app.service.transaction;

import com.expensetrace.app.dto.response.transaction.*;
import com.expensetrace.app.dto.response.transaction.record.DebtAdjustmentResponseDto;
import com.expensetrace.app.dto.response.transaction.record.DebtPaidResponseDto;
import com.expensetrace.app.dto.response.transaction.record.DebtReceivedResponseDto;
import com.expensetrace.app.enums.TransactionType;
import com.expensetrace.app.model.User;
import com.expensetrace.app.model.transaction.record.AdjustmentRecord;
import com.expensetrace.app.repository.transaction.*;
import com.expensetrace.app.repository.transaction.debt.AdjustmentRecordRepository;
import com.expensetrace.app.repository.transaction.debt.PaidRecordRepository;
import com.expensetrace.app.repository.transaction.debt.ReceivedRecordRepository;
import com.expensetrace.app.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class TransactionAggregationService {

    private final IncomeTransactionRepository incomeRepo;
    private final ExpenseTransactionRepository expenseRepo;
    private final TransferTransactionRepository transferRepo;
    private final AdjustmentTransactionRepository adjustmentRepo;

    private final PaidRecordRepository paidRepo;
    private final ReceivedRecordRepository receivedRepo;
    private final AdjustmentRecordRepository debtAdjustmentRepo;

    private final ModelMapper mapper;
    private final UserService userService;

    /**
     * Utility to combine txnDate + txnTime into LocalDateTime for sorting
     */
    private LocalDateTime getTxnDateTime(TransactionResponseDto dto) {
        if (dto.getTxnDate() == null) {
            return LocalDateTime.MIN; // fallback if somehow missing
        }
        return dto.getTxnTime() != null
                ? dto.getTxnDate().atTime(dto.getTxnTime())
                : dto.getTxnDate().atStartOfDay();
    }

    // Standard transactions: Income, Expense, Transfer
    public Page<TransactionResponseDto> getAllStandardTransactions(Pageable pageable) {
        User user = userService.getAuthenticatedUser();
        UUID userId = user.getId();
        List<? extends TransactionResponseDto> income = incomeRepo.findAllByUserId(userId)
                .stream().map(e -> mapper.map(e, IncomeTransactionResponseDto.class)).toList();

        List<? extends TransactionResponseDto> expense = expenseRepo.findAllByUserId(userId)
                .stream().map(e -> mapper.map(e, ExpenseTransactionResponseDto.class)).toList();

        List<? extends TransactionResponseDto> transfer = transferRepo.findAllByUserId(userId)
                .stream().map(e -> mapper.map(e, TransferTransactionResponseDto.class)).toList();

        List<TransactionResponseDto> merged = Stream.<List<? extends TransactionResponseDto>>of(income, expense, transfer)
                .flatMap(Collection::stream)
                .map(TransactionResponseDto.class::cast)
                .sorted(Comparator.comparing(
                        this::getTxnDateTime,
                        Comparator.nullsLast(Comparator.naturalOrder())
                ).reversed())
                .toList();

        return toPage(merged, pageable);
    }

    // Debt transactions: Paid, Received
    public Page<TransactionResponseDto> getAllDebtTransactions(UUID debtId, Pageable pageable) {
        List<? extends TransactionResponseDto> paid = paidRepo.findAllByDebtId(debtId)
                .stream().map(e -> mapper.map(e, DebtPaidResponseDto.class)).toList();

        List<? extends TransactionResponseDto> received = receivedRepo.findAllByDebtId(debtId)
                .stream().map(e -> mapper.map(e, DebtReceivedResponseDto.class)).toList();

        List<TransactionResponseDto> merged = Stream.<List<? extends TransactionResponseDto>>of(paid, received)
                .flatMap(Collection::stream)
                .map(TransactionResponseDto.class::cast)
                .sorted(Comparator.comparing(
                        this::getTxnDateTime,
                        Comparator.nullsLast(Comparator.naturalOrder())
                ).reversed())
                .toList();
        return toPage(merged, pageable);
    }

    // Debt transactions: Paid, Received
    public Page<TransactionResponseDto> getAllDebtPaidTransactions(UUID debtId,Pageable pageable) {
        List<? extends TransactionResponseDto> paid = paidRepo.findAllByDebtId(debtId)
                .stream().map(e -> mapper.map(e, DebtPaidResponseDto.class)).toList();

        List<TransactionResponseDto> merged= Stream.<List<? extends TransactionResponseDto>>of(paid)
                .flatMap(Collection::stream)
                .map(TransactionResponseDto.class::cast)
                .sorted(Comparator.comparing(
                        this::getTxnDateTime,
                        Comparator.nullsLast(Comparator.naturalOrder())
                ).reversed())
                .toList();
        return toPage(merged, pageable);
    }

    public Page<TransactionResponseDto> getAllDebtReceivedTransactions(UUID debtId,Pageable pageable) {
        List<? extends TransactionResponseDto> received = receivedRepo.findAllByDebtId(debtId)
                .stream().map(e -> mapper.map(e, DebtReceivedResponseDto.class)).toList();

        List<TransactionResponseDto> merged= Stream.<List<? extends TransactionResponseDto>>of(received)
                .flatMap(Collection::stream)
                .map(TransactionResponseDto.class::cast)
                .sorted(Comparator.comparing(
                        this::getTxnDateTime,
                        Comparator.nullsLast(Comparator.naturalOrder())
                ).reversed())
                .toList();
        return toPage(merged, pageable);
    }

    public Page<TransactionResponseDto> getAllDebtAdjustmentTransactions(UUID debtId,Pageable pageable) {
        List<? extends TransactionResponseDto> received = debtAdjustmentRepo.findAllByDebtId(debtId)
                .stream().map(e -> mapper.map(e, DebtAdjustmentResponseDto.class)).toList();

        List<TransactionResponseDto> merged= Stream.<List<? extends TransactionResponseDto>>of(received)
                .flatMap(Collection::stream)
                .map(TransactionResponseDto.class::cast)
                .sorted(Comparator.comparing(
                        this::getTxnDateTime,
                        Comparator.nullsLast(Comparator.naturalOrder())
                ).reversed())
                .toList();
        return toPage(merged, pageable);
    }

    // 3. All transactions for a specific account
    public Page<TransactionResponseDto> getAllTransactionsByAccount(UUID accountId,Pageable pageable) {
        List<IncomeTransactionResponseDto> income = incomeRepo.findByAccountId(accountId)
                .stream().map(e -> mapper.map(e, IncomeTransactionResponseDto.class)).toList();
        List<ExpenseTransactionResponseDto> expense = expenseRepo.findByAccountId(accountId)
                .stream().map(e -> mapper.map(e, ExpenseTransactionResponseDto.class)).toList();
        List<TransferTransactionResponseDto> transfer = transferRepo.findByFromAccountIdOrToAccountId(accountId, accountId)
                .stream().map(e -> mapper.map(e, TransferTransactionResponseDto.class)).toList();
        List<AdjustmentTransactionResponseDto> adjustment = adjustmentRepo.findByAccountId(accountId)
                .stream().map(e -> mapper.map(e, AdjustmentTransactionResponseDto.class)).toList();
        List<DebtPaidResponseDto> debtPaid = paidRepo.findByAccountId(accountId)
                .stream().map(e -> mapper.map(e, DebtPaidResponseDto.class)).toList();
        List<DebtReceivedResponseDto> debtReceived = receivedRepo.findByAccountId(accountId)
                .stream().map(e -> mapper.map(e, DebtReceivedResponseDto.class)).toList();

        List<TransactionResponseDto> merged = Stream.<List<? extends TransactionResponseDto>>of(income, expense, transfer, adjustment, debtPaid, debtReceived)
                .flatMap(Collection::stream)
                .map(TransactionResponseDto.class::cast)
                .sorted(Comparator.comparing(
                        this::getTxnDateTime,
                        Comparator.nullsLast(Comparator.naturalOrder())
                ).reversed())
                .toList();
        return toPage(merged, pageable);
    }

    public Page<TransactionResponseDto> getAllCreditTransactionsByAccount(UUID accountId,Pageable pageable) {
        List<IncomeTransactionResponseDto> income = incomeRepo.findByAccountId(accountId)
                .stream().map(e -> mapper.map(e, IncomeTransactionResponseDto.class)).toList();
        List<TransferTransactionResponseDto> transfer = transferRepo.findByToAccountId(accountId)
                .stream().map(e -> mapper.map(e, TransferTransactionResponseDto.class)).toList();
        List<DebtReceivedResponseDto> debtReceived = receivedRepo.findByAccountId(accountId)
                .stream().map(e -> mapper.map(e, DebtReceivedResponseDto.class)).toList();

        List<TransactionResponseDto> merged = Stream.<List<? extends TransactionResponseDto>>of(income, transfer, debtReceived)
                .flatMap(Collection::stream)
                .map(TransactionResponseDto.class::cast)
                .sorted(Comparator.comparing(
                        this::getTxnDateTime,
                        Comparator.nullsLast(Comparator.naturalOrder())
                ).reversed())
                .toList();
        return toPage(merged, pageable);
    }

    public Page<TransactionResponseDto> getAllDebitTransactionsByAccount(UUID accountId,Pageable pageable) {
        List<ExpenseTransactionResponseDto> expense = expenseRepo.findByAccountId(accountId)
                .stream().map(e -> mapper.map(e, ExpenseTransactionResponseDto.class)).toList();
        List<TransferTransactionResponseDto> transfer = transferRepo.findByFromAccountId(accountId)
                .stream().map(e -> mapper.map(e, TransferTransactionResponseDto.class)).toList();
        List<DebtPaidResponseDto> debtPaid = paidRepo.findByAccountId(accountId)
                .stream().map(e -> mapper.map(e, DebtPaidResponseDto.class)).toList();
        List<TransactionResponseDto> merged = Stream.<List<? extends TransactionResponseDto>>of(expense, transfer, debtPaid)
                .flatMap(Collection::stream)
                .map(TransactionResponseDto.class::cast)
                .sorted(Comparator.comparing(
                        this::getTxnDateTime,
                        Comparator.nullsLast(Comparator.naturalOrder())
                ).reversed())
                .toList();
        return toPage(merged, pageable);
    }

    public Page<TransactionResponseDto> getAllAdjustmentTransactionsByAccount(UUID accountId,Pageable pageable) {
        List<AdjustmentTransactionResponseDto> adjustment = adjustmentRepo.findByAccountId(accountId)
                .stream().map(e -> mapper.map(e, AdjustmentTransactionResponseDto.class)).toList();
        List<TransactionResponseDto> merged = Stream.<List<? extends TransactionResponseDto>>of(adjustment)
                .flatMap(Collection::stream)
                .map(TransactionResponseDto.class::cast)
                .sorted(Comparator.comparing(
                        this::getTxnDateTime,
                        Comparator.nullsLast(Comparator.naturalOrder())
                ).reversed())
                .toList();
        return toPage(merged, pageable);
    }

    public List<TransactionResponseDto> getRecentTransactions(int limit) {
        User user = userService.getAuthenticatedUser();
        UUID userId = user.getId();
        List<? extends TransactionResponseDto> income = incomeRepo.findAllByUserId(userId)
                .stream().map(e -> mapper.map(e, IncomeTransactionResponseDto.class)).toList();

        List<? extends TransactionResponseDto> expense = expenseRepo.findAllByUserId(userId)
                .stream().map(e -> mapper.map(e, ExpenseTransactionResponseDto.class)).toList();

        List<? extends TransactionResponseDto> transfer = transferRepo.findAllByUserId(userId)
                .stream().map(e -> mapper.map(e, TransferTransactionResponseDto.class)).toList();

        return Stream.of(income, expense, transfer)
                .flatMap(Collection::stream)
                .map(TransactionResponseDto.class::cast)
                .sorted(Comparator.comparing(
                        this::getTxnDateTime,
                        Comparator.nullsLast(Comparator.naturalOrder())
                ).reversed())
                .limit(limit)
                .toList();
    }

    public TransactionDateSummaryDto getTransactionsByDate(LocalDate date, Pageable pageable) {
        User user = userService.getAuthenticatedUser();
        UUID userId = user.getId();

        // Fetch all types of transactions for the user on that date
        List<TransactionResponseDto> allTxns = Stream.of(
                        incomeRepo.findByUserIdAndTxnDate(userId, date)
                                .stream().map(e -> mapper.map(e, IncomeTransactionResponseDto.class)).toList(),
                        expenseRepo.findByUserIdAndTxnDate(userId, date)
                                .stream().map(e -> mapper.map(e, ExpenseTransactionResponseDto.class)).toList(),
                        transferRepo.findByUserIdAndTxnDate(userId, date)
                                .stream().map(e -> mapper.map(e, TransferTransactionResponseDto.class)).toList()
                )
                .flatMap(List::stream)
                .map(TransactionResponseDto.class::cast)
                .sorted(Comparator.comparing(TransactionResponseDto::getTxnDate)
                        .thenComparing(TransactionResponseDto::getTxnTime)
                        .reversed())
                .toList();

        // Calculate totals
        BigDecimal totalIncome = allTxns.stream()
                .filter(t -> t.getType() == TransactionType.INCOME || t.getType() == TransactionType.DEBT_RECEIVED)
                .map(TransactionResponseDto::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpense = allTxns.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE || t.getType() == TransactionType.DEBT_PAID)
                .map(TransactionResponseDto::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Paginate transactions
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), allTxns.size());
        Page<TransactionResponseDto> pagedTxns = new PageImpl<>(start > allTxns.size() ? List.of() : allTxns.subList(start, end), pageable, allTxns.size());

        TransactionDateSummaryDto response = new TransactionDateSummaryDto();
        response.setTotalIncome(totalIncome);
        response.setTotalExpense(totalExpense);
        response.setTransactions(pagedTxns);

        return response;
    }

    // Helper to convert List to Page
    private Page<TransactionResponseDto> toPage(List<TransactionResponseDto> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), list.size());

        List<TransactionResponseDto> subList = start > list.size() ? List.of() : list.subList(start, end);

        return new PageImpl<>(subList, pageable, list.size());
    }
}