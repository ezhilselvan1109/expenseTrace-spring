package com.expensetrace.app.service.budget.strategy;

import com.expensetrace.app.dto.request.budget.BudgetRequestDto;
import com.expensetrace.app.dto.request.budget.MonthlyBudgetRequestDto;
import com.expensetrace.app.dto.response.CategorySpendResponseDto;
import com.expensetrace.app.dto.response.budget.BudgetListResponseDto;
import com.expensetrace.app.dto.response.budget.BudgetResponseDto;
import com.expensetrace.app.dto.response.budget.MonthlyBudgetBreakdownResponseDto;
import com.expensetrace.app.dto.response.budget.MonthlyBudgetSummaryResponseDto;
import com.expensetrace.app.model.User;
import com.expensetrace.app.model.budget.MonthlyBudget;
import com.expensetrace.app.repository.MonthlyBudgetRepository;
import com.expensetrace.app.repository.UserRepository;
import com.expensetrace.app.repository.transaction.ExpenseTransactionRepository;
import com.expensetrace.app.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MonthlyBudgetStrategy implements BudgetStrategy {

    private final MonthlyBudgetRepository repository;
    private final ExpenseTransactionRepository expenseTransactionRepository;
    private final UserRepository userRepository;
    private final SecurityUtil securityUtil;
    private final ModelMapper mapper;

    @Override
    public BudgetResponseDto create(BudgetRequestDto dto) {
        UUID userId = securityUtil.getAuthenticatedUserId();
        MonthlyBudgetRequestDto monthlyDto = (MonthlyBudgetRequestDto) dto;
        MonthlyBudget budget = mapper.map(monthlyDto, MonthlyBudget.class);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        budget.setUser(user);

        budget.setId(UUID.randomUUID());
        MonthlyBudget saved = repository.save(budget);
        return mapper.map(saved, BudgetResponseDto.class);
    }

    @Override
    public BudgetListResponseDto getAll() {
        List<MonthlyBudget> budgets = repository.findAll();
        int currentYear = LocalDate.now().getYear();
        int currentMonth = LocalDate.now().getMonthValue();

        List<BudgetResponseDto> past = new ArrayList<>();
        List<BudgetResponseDto> present = new ArrayList<>();
        List<BudgetResponseDto> upcoming = new ArrayList<>();

        for (MonthlyBudget b : budgets) {
            Double spent = expenseTransactionRepository.getMonthlySpent(
                    b.getUser().getId(),
                    b.getYear(),
                    b.getMonth()
            );
            MonthlyBudgetSummaryResponseDto dto = mapper.map(b, MonthlyBudgetSummaryResponseDto.class);
            dto.setTotalSpent(spent);
            if (b.getYear() < currentYear ||
                    (b.getYear() == currentYear && b.getMonth() < currentMonth)) {
                past.add(dto);
            } else if (b.getYear() == currentYear && b.getMonth() == currentMonth) {
                present.add(dto);
            } else {
                upcoming.add(dto);
            }
        }

        BudgetListResponseDto response = new BudgetListResponseDto();
        response.setPast(past);
        response.setPresent(present);
        response.setUpcoming(upcoming);
        return response;
    }

    @Override
    public BudgetResponseDto update(UUID id, BudgetRequestDto dto) {
        MonthlyBudget budget = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Monthly budget not found"));

        MonthlyBudgetRequestDto monthlyDto = (MonthlyBudgetRequestDto) dto;
        budget.setYear(monthlyDto.getYear());
        budget.setMonth(monthlyDto.getMonth());
        budget.setTotalLimit(monthlyDto.getTotalLimit());

        MonthlyBudget updated = repository.save(budget);
        return mapper.map(updated, BudgetResponseDto.class);
    }

    @Override
    public void delete(UUID budgetId) {
        repository.deleteById(budgetId);
    }
}