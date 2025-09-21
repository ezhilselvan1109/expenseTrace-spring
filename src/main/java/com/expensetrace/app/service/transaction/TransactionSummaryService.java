package com.expensetrace.app.service.transaction;

import com.expensetrace.app.dto.response.transaction.TransactionSummaryDto;
import com.expensetrace.app.enums.TransactionSummaryType;
import com.expensetrace.app.model.User;
import com.expensetrace.app.repository.transaction.ExpenseTransactionRepository;
import com.expensetrace.app.repository.transaction.IncomeTransactionRepository;
import com.expensetrace.app.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;

@Service
@RequiredArgsConstructor
public class TransactionSummaryService {

    private final IncomeTransactionRepository incomeRepo;
    private final ExpenseTransactionRepository expenseRepo;
    private final UserService userService;

    public TransactionSummaryDto getSummary(TransactionSummaryType rangeType) {
        User user = userService.getAuthenticatedUser();
        LocalDate now = LocalDate.now();
        LocalDate startDate = null;
        LocalDate endDate = null;

        switch (rangeType) {
            case MONTH -> {
                YearMonth month = YearMonth.now();
                startDate = month.atDay(1);
                endDate = month.atEndOfMonth();
            }
            case YEAR -> {
                Year year = Year.now();
                startDate = year.atDay(1);
                endDate = year.atDay(year.length());
            }
            case ALL_TIME -> {
                startDate = null;
                endDate = null;
            }
            default -> throw new IllegalArgumentException("Invalid range type: " + rangeType);
        }

        BigDecimal income = incomeRepo.getTotalIncome(user.getId(), startDate, endDate);
        BigDecimal expense = expenseRepo.getTotalExpense(user.getId(), startDate, endDate);

        return new TransactionSummaryDto(income, expense);
    }
}
