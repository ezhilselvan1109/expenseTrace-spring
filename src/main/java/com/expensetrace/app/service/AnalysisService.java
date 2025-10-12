package com.expensetrace.app.service;

import com.expensetrace.app.dto.response.CategoryResponseDto;
import com.expensetrace.app.dto.response.account.AccountResponseDto;
import com.expensetrace.app.dto.response.summary.*;
import com.expensetrace.app.enums.AnalysisType;
import com.expensetrace.app.enums.TransactionType;
import com.expensetrace.app.model.User;
import com.expensetrace.app.model.transaction.*;
import com.expensetrace.app.repository.transaction.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalysisService {

    private final TransactionRepository transactionRepository;
    private final ModelMapper modelMapper;

    public AnalysisSummaryResponseDto getAnalysis(
            AnalysisType type,
            Integer year,
            Integer month,
            LocalDate from,
            LocalDate to
    ) {
        LocalDate startDate;
        LocalDate endDate;

        switch (type) {
            case WEEK, CUSTOM -> {
                startDate = from;
                endDate = (to != null) ? to : LocalDate.now();
            }
            case MONTH -> {
                startDate = LocalDate.of(year, month, 1);
                endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
            }
            case YEAR -> {
                startDate = LocalDate.of(year, 1, 1);
                endDate = LocalDate.of(year, 12, 31);
            }
            default -> throw new IllegalArgumentException("Invalid analysis type: " + type);
        }

        return buildSummary(startDate, endDate);
    }

    private AnalysisSummaryResponseDto buildSummary(LocalDate from, LocalDate to) {
        List<Transaction> transactions = transactionRepository.findByTxnDateBetween(from, to);

        int totalSpending = sumAmount(transactions, TransactionType.EXPENSE);
        int totalIncome = sumAmount(transactions, TransactionType.INCOME);

        AnalysisSummaryResponseDto dto = new AnalysisSummaryResponseDto();
        dto.setSpending(totalSpending);
        dto.setIncome(totalIncome);
        dto.setNumberOfTransactions(transactions.size());

        long days = ChronoUnit.DAYS.between(from, to) + 1;

        dto.setAverageSpendingPerDay(days > 0 ? totalSpending / (int) days : 0);
        dto.setAverageIncomePerDay(days > 0 ? totalIncome / (int) days : 0);

        long spendingCount = transactions.stream().filter(t -> t.getType() == TransactionType.EXPENSE).count();
        long incomeCount = transactions.stream().filter(t -> t.getType() == TransactionType.INCOME).count();

        dto.setAverageSpendingPerTransaction(spendingCount > 0 ? totalSpending / (int) spendingCount : 0);
        dto.setAverageIncomePerTransaction(incomeCount > 0 ? totalIncome / (int) incomeCount : 0);

        // Group by category
        dto.setSpendingCategory(buildCategorySummary(transactions, ExpenseTransaction.class));
        dto.setIncomeCategory(buildCategorySummary(transactions, IncomeTransaction.class));

        // Group by account
        dto.setSpendingAccount(buildAccountSummary(transactions, ExpenseTransaction.class, true));
        dto.setIncomeAccount(buildAccountSummary(transactions, IncomeTransaction.class, true));
        dto.setTransfersAccount(buildAccountSummary(transactions, TransferTransaction.class, false));

        return dto;
    }

    private int sumAmount(List<Transaction> transactions, TransactionType type) {
        return transactions.stream()
                .filter(t -> t.getType() == type)
                .map(Transaction::getAmount)
                .mapToInt(BigDecimal::intValue)
                .sum();
    }

    private <T extends Transaction> List<CategorySummary> buildCategorySummary(
            List<Transaction> transactions, Class<T> clazz
    ) {
        return transactions.stream()
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .collect(Collectors.groupingBy(
                        t -> (t instanceof ExpenseTransaction) ? ((ExpenseTransaction) t).getCategory() : ((IncomeTransaction) t).getCategory(),
                        Collectors.mapping(Transaction::getAmount,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ))
                .entrySet().stream()
                .map(entry -> {
                    CategorySummary cs = new CategorySummary();
                    cs.setCategory(modelMapper.map(entry.getKey(), CategoryResponseDto.class));
                    cs.setAmount(entry.getValue().intValue());
                    return cs;
                })
                .toList();
    }

    private <T extends Transaction> List<AccountSummary> buildAccountSummary(
            List<Transaction> transactions, Class<T> clazz, boolean singleAccount
    ) {
        return transactions.stream()
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .collect(Collectors.groupingBy(
                        t -> {
                            if (t instanceof ExpenseTransaction et) return et.getAccount();
                            if (t instanceof IncomeTransaction it) return it.getAccount();
                            if (t instanceof TransferTransaction tt) {
                                return singleAccount ? tt.getFromAccount() : tt.getToAccount();
                            }
                            return null;
                        },
                        Collectors.mapping(Transaction::getAmount,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ))
                .entrySet().stream()
                .filter(e -> e.getKey() != null)
                .map(entry -> {
                    AccountSummary as = new AccountSummary();
                    as.setAccountResponseDto(modelMapper.map(entry.getKey(), AccountResponseDto.class));
                    as.setAmount(entry.getValue().intValue());
                    return as;
                })
                .toList();
    }
}
