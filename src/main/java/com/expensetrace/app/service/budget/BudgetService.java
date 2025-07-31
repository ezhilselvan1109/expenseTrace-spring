package com.expensetrace.app.service.budget;

import com.expensetrace.app.dto.response.*;
import com.expensetrace.app.exception.AccessDeniedException;
import com.expensetrace.app.model.*;
import com.expensetrace.app.model.budget.Budget;
import com.expensetrace.app.model.budget.CategoryBudgetLimit;
import com.expensetrace.app.model.budget.MonthlyBudget;
import com.expensetrace.app.model.budget.YearlyBudget;
import com.expensetrace.app.repository.*;
import com.expensetrace.app.dto.request.CategoryLimitDto;
import com.expensetrace.app.dto.request.MonthlyBudgetRequestDto;
import com.expensetrace.app.dto.request.YearlyBudgetRequestDto;
import com.expensetrace.app.util.SecurityUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BudgetService implements IBudgetService {

    private final SecurityUtil securityUtil;
    private final MonthlyBudgetRepository monthlyBudgetRepo;
    private final YearlyBudgetRepository yearlyBudgetRepo;
    private final CategoryRepository categoryRepo;
    private final BudgetRepository budgetRepository;
    private final TransactionRepository transactionRepo;

    @Override
    @Transactional
    public void createMonthlyBudget(MonthlyBudgetRequestDto dto) {
        UUID userId = securityUtil.getAuthenticatedUserId();

        MonthlyBudget budget = new MonthlyBudget();
        budget.setId(UUID.randomUUID());
        budget.setYear(dto.getYear());
        budget.setMonth(dto.getMonth());
        budget.setTotalLimit(dto.getTotalLimit());
        User user=new User();
        user.setId(userId);
        budget.setUser(user);

        Set<CategoryBudgetLimit> categoryLimits = new HashSet<>();
        for (CategoryLimitDto limitDto : dto.getCategoryLimits()) {
            Category category = categoryRepo.findById(limitDto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));

            CategoryBudgetLimit limit = new CategoryBudgetLimit();
            limit.setCategory(category);
            limit.setBudget(budget);
            limit.setCategoryLimit(limitDto.getCategoryLimit());

            categoryLimits.add(limit);
        }

        budget.setCategoryLimits(categoryLimits);
        monthlyBudgetRepo.save(budget);
    }

    @Override
    @Transactional
    public void createYearlyBudget(YearlyBudgetRequestDto dto) {
        UUID userId = securityUtil.getAuthenticatedUserId();

        YearlyBudget budget = new YearlyBudget();
        budget.setId(UUID.randomUUID());
        budget.setYear(dto.getYear());
        budget.setTotalLimit(dto.getTotalLimit());
        User user=new User();
        user.setId(userId);
        budget.setUser(user);

        Set<CategoryBudgetLimit> categoryLimits = new HashSet<>();
        for (CategoryLimitDto limitDto : dto.getCategoryLimits()) {
            Category category = categoryRepo.findById(limitDto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));

            CategoryBudgetLimit limit = new CategoryBudgetLimit();
            limit.setCategory(category);
            limit.setBudget(budget);
            limit.setCategoryLimit(limitDto.getCategoryLimit());

            categoryLimits.add(limit);
        }

        budget.setCategoryLimits(categoryLimits);
        yearlyBudgetRepo.save(budget);
    }

    @Override
    @Transactional
    public void updateMonthlyBudget(UUID id, MonthlyBudgetRequestDto request) {
        MonthlyBudget budget = monthlyBudgetRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Monthly budget not found"));

        UUID userId = securityUtil.getAuthenticatedUserId();
        if (!budget.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("Not authorized");
        }

        budget.setYear(request.getYear());
        budget.setMonth(request.getMonth());
        budget.setTotalLimit(request.getTotalLimit());

        budget.getCategoryLimits().clear();

        for (CategoryLimitDto dto : request.getCategoryLimits()) {
            Category category = categoryRepo.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            CategoryBudgetLimit limit = new CategoryBudgetLimit();
            limit.setBudget(budget);
            limit.setCategory(category);
            limit.setCategoryLimit(dto.getCategoryLimit());
            budget.getCategoryLimits().add(limit);
        }

        monthlyBudgetRepo.save(budget);
    }

    @Override
    @Transactional
    public void updateYearlyBudget(UUID id, YearlyBudgetRequestDto request) {
        YearlyBudget budget = yearlyBudgetRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Yearly budget not found"));

        UUID userId = securityUtil.getAuthenticatedUserId();
        if (!budget.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("Not authorized");
        }

        budget.setYear(request.getYear());
        budget.setTotalLimit(request.getTotalLimit());

        budget.getCategoryLimits().clear();

        for (CategoryLimitDto dto : request.getCategoryLimits()) {
            Category category = categoryRepo.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            CategoryBudgetLimit limit = new CategoryBudgetLimit();
            limit.setBudget(budget);
            limit.setCategory(category);
            limit.setCategoryLimit(dto.getCategoryLimit());
            budget.getCategoryLimits().add(limit);
        }

        yearlyBudgetRepo.save(budget);
    }


    @Override
    @Transactional
    public void deleteBudget(UUID id) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Budget not found"));

        UUID userId = securityUtil.getAuthenticatedUserId();
        if (!budget.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You are not authorized to delete this budget.");
        }

        budgetRepository.delete(budget);
    }

    @Override
    public Map<String, List<MonthlyBudgetSummaryResponseDto>> getMonthlyBudgetSummary() {
        UUID userId = securityUtil.getAuthenticatedUserId();
        List<MonthlyBudget> budgets = monthlyBudgetRepo.findAllByUserId(userId);

        LocalDate today = LocalDate.now();

        Map<String, List<MonthlyBudgetSummaryResponseDto>> result = new HashMap<>();
        result.put("past", new ArrayList<>());
        result.put("active", new ArrayList<>());
        result.put("upcoming", new ArrayList<>());

        for (MonthlyBudget b : budgets) {
            LocalDate budgetDate = LocalDate.of(b.getYear(), b.getMonth(), 1);
            double spent = transactionRepo.sumAmountByUserIdAndMonthAndYear(userId, b.getMonth(), b.getYear());

            MonthlyBudgetSummaryResponseDto dto = new MonthlyBudgetSummaryResponseDto(b.getId(),
                    b.getMonth(), b.getYear(), b.getTotalLimit(), spent
            );

            if (budgetDate.getYear() == today.getYear() && budgetDate.getMonth() == today.getMonth()) {
                result.get("active").add(dto);
            } else if (budgetDate.isBefore(today.withDayOfMonth(1))) {
                result.get("past").add(dto);
            } else {
                result.get("upcoming").add(dto);
            }
        }

        return result;
    }

    @Override
    public Map<String, List<YearlyBudgetSummaryResponseDto>> getYearlyBudgetSummary() {
        UUID userId = securityUtil.getAuthenticatedUserId();
        List<YearlyBudget> budgets = yearlyBudgetRepo.findAllByUserId(userId);

        int currentYear = LocalDate.now().getYear();

        Map<String, List<YearlyBudgetSummaryResponseDto>> result = new HashMap<>();
        result.put("past", new ArrayList<>());
        result.put("active", new ArrayList<>());
        result.put("upcoming", new ArrayList<>());

        for (YearlyBudget b : budgets) {
            double spent = transactionRepo.sumAmountByUserIdAndYear(userId, b.getYear());

            YearlyBudgetSummaryResponseDto dto = new YearlyBudgetSummaryResponseDto(b.getId(),
                    b.getYear(), b.getTotalLimit(), spent
            );

            if (b.getYear() == currentYear) {
                result.get("active").add(dto);
            } else if (b.getYear() < currentYear) {
                result.get("past").add(dto);
            } else {
                result.get("upcoming").add(dto);
            }
        }

        return result;
    }

    @Override
    public MonthlyBudgetBreakdownResponseDto getMonthlyBudgetBreakdown(UUID budgetId) {
        MonthlyBudget budget = monthlyBudgetRepo.findById(budgetId)
                .orElseThrow(() -> new RuntimeException("Monthly Budget not found"));

        double totalSpent = 0;
        List<CategorySpendResponseDto> categorySpends = new ArrayList<>();

        for (CategoryBudgetLimit limit : budget.getCategoryLimits()) {
            Category category = limit.getCategory();

            double spent = transactionRepo.sumByCategoryAndMonthAndYear(
                    category.getId(), budget.getMonth(), budget.getYear(), budget.getUser().getId());

            totalSpent += spent;

            categorySpends.add(new CategorySpendResponseDto(
                    category.getId(),
                    category.getName(),
                    category.getColor(),
                    category.getIcon(),
                    limit.getCategoryLimit(),
                    spent
            ));
        }

        return new MonthlyBudgetBreakdownResponseDto(
                budget.getId(),
                budget.getMonth(),
                budget.getYear(),
                budget.getTotalLimit(),
                totalSpent,
                categorySpends
        );
    }

    @Override
    public YearlyBudgetBreakdownResponseDto getYearlyBudgetBreakdown(UUID budgetId) {
        YearlyBudget budget = yearlyBudgetRepo.findById(budgetId)
                .orElseThrow(() -> new RuntimeException("Yearly Budget not found"));

        double totalSpent = 0;
        List<CategorySpendResponseDto> categorySpends = new ArrayList<>();

        for (CategoryBudgetLimit limit : budget.getCategoryLimits()) {
            Category category = limit.getCategory();

            double spent = transactionRepo.sumByCategoryAndYear(
                    category.getId(), budget.getYear(), budget.getUser().getId());

            totalSpent += spent;

            categorySpends.add(new CategorySpendResponseDto(
                    category.getId(),
                    category.getName(),
                    category.getColor(),
                    category.getIcon(),
                    limit.getCategoryLimit(),
                    spent
            ));
        }

        return new YearlyBudgetBreakdownResponseDto(
                budget.getId(),
                budget.getYear(),
                budget.getTotalLimit(),
                totalSpent,
                categorySpends
        );
    }

}
