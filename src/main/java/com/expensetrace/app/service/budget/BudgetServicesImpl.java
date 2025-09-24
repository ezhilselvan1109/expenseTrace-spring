package com.expensetrace.app.service.budget;

import com.expensetrace.app.dto.request.budget.MonthlyBudgetRequestDto;
import com.expensetrace.app.dto.request.budget.YearlyBudgetRequestDto;
import com.expensetrace.app.dto.response.budget.CategorySpendResponseDto;
import com.expensetrace.app.dto.response.budget.*;
import com.expensetrace.app.model.User;
import com.expensetrace.app.model.budget.*;
import com.expensetrace.app.model.Category;
import com.expensetrace.app.repository.*;
import com.expensetrace.app.repository.transaction.ExpenseTransactionRepository;
import com.expensetrace.app.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BudgetServicesImpl implements BudgetServices {

    private final MonthlyBudgetRepository monthlyBudgetRepo;
    private final BudgetRepository budgetRepo;
    private final YearlyBudgetRepository yearlyBudgetRepo;
    private final ExpenseTransactionRepository expenseTransactionRepo;
    private final CategoryRepository categoryRepo;
    private final ModelMapper mapper;
    private final UserService userService;

    @Override
    public BudgetResponseDto createMonthlyBudget(MonthlyBudgetRequestDto dto) {
        User user = userService.getAuthenticatedUser();

        MonthlyBudget budget = new MonthlyBudget();
        budget.setYear(dto.getYear());
        budget.setMonth(dto.getMonth());
        budget.setTotalLimit(BigDecimal.valueOf(dto.getTotalLimit()));
        budget.setUser(user);

        budget.setCategoryLimits(dto.getCategoryLimits().stream().map(cl -> {
            Category category = categoryRepo.findById(cl.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            CategoryBudgetLimit limit = new CategoryBudgetLimit();
            limit.setBudget(budget);
            limit.setCategory(category);
            limit.setCategoryLimit(BigDecimal.valueOf(cl.getCategoryLimit()));
            return limit;
        }).collect(Collectors.toSet()));

        MonthlyBudget saved = monthlyBudgetRepo.save(budget);

        // map
        BudgetResponseDto response = mapper.map(saved, BudgetResponseDto.class);

        // calculate spent (sum of expense transactions for this user/year/month/categories)
        BigDecimal totalSpent = expenseTransactionRepo.sumSpentByUserAndYearAndMonth(
                user.getId(), saved.getYear(), saved.getMonth()
        );
        response.setTotalSpent(totalSpent != null ? totalSpent.doubleValue() : 0.0);

        return response;
    }

    @Override
    public BudgetResponseDto createYearlyBudget(YearlyBudgetRequestDto dto) {

        User user = userService.getAuthenticatedUser();

        YearlyBudget budget = new YearlyBudget();
        budget.setYear(dto.getYear());
        budget.setTotalLimit(BigDecimal.valueOf(dto.getTotalLimit()));
        budget.setUser(user);

        budget.setCategoryLimits(dto.getCategoryLimits().stream().map(cl -> {
            Category category = categoryRepo.findById(cl.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            CategoryBudgetLimit limit = new CategoryBudgetLimit();
            limit.setBudget(budget);
            limit.setCategory(category);
            limit.setCategoryLimit(BigDecimal.valueOf(cl.getCategoryLimit()));
            return limit;
        }).collect(Collectors.toSet()));

        YearlyBudget saved = yearlyBudgetRepo.save(budget);

        // map
        BudgetResponseDto response = mapper.map(saved, BudgetResponseDto.class);

        // calculate spent (sum of expense transactions for this user/year/month/categories)
        BigDecimal totalSpent = expenseTransactionRepo.sumSpentByUserAndYear(
                user.getId(), saved.getYear()
        );
        response.setTotalSpent(totalSpent != null ? totalSpent.doubleValue() : 0.0);

        return response;
    }

    private MonthlyBudgetResponseDto mapMonthlyWithSpent(MonthlyBudget budget, UUID userId) {
        MonthlyBudgetResponseDto dto = mapper.map(budget, MonthlyBudgetResponseDto.class);
        BigDecimal spent = expenseTransactionRepo.sumSpentByUserAndYearAndMonth(
                userId, budget.getYear(), budget.getMonth()
        );
        dto.setTotalSpent(spent != null ? spent.doubleValue() : 0.0);
        return dto;
    }

    private BudgetResponseDto mapYearlyWithSpent(YearlyBudget budget, UUID userId) {
        BudgetResponseDto dto = mapper.map(budget, BudgetResponseDto.class);
        BigDecimal spent = expenseTransactionRepo.sumSpentByUserAndYear(
                userId, budget.getYear()
        );
        dto.setTotalSpent(spent != null ? spent.doubleValue() : 0.0);
        return dto;
    }

    @Override
    public BudgetListResponseDto getMonthlyBudgetList() {
        LocalDate today = LocalDate.now();
        int currentYear = today.getYear();
        int currentMonth = today.getMonthValue();
        UUID userId = userService.getAuthenticatedUser().getId();

        List<MonthlyBudget> all = monthlyBudgetRepo.findAll();

        List<BudgetResponseDto> past = all.stream()
                .filter(b -> b.getYear() < currentYear ||
                        (b.getYear() == currentYear && b.getMonth() < currentMonth))
                .map(b -> {
                    MonthlyBudgetResponseDto summary = mapMonthlyWithSpent(b, userId);
                    return mapper.map(summary, BudgetResponseDto.class);
                })
                .collect(Collectors.toList());

        BudgetResponseDto present = all.stream()
                .filter(b -> b.getYear() == currentYear && b.getMonth() == currentMonth)
                .findFirst()
                .map(b -> {
                    MonthlyBudgetResponseDto summary = mapMonthlyWithSpent(b, userId);
                    return mapper.map(summary, BudgetResponseDto.class);
                })
                .orElse(null);

        List<BudgetResponseDto> upcoming = all.stream()
                .filter(b -> b.getYear() > currentYear ||
                        (b.getYear() == currentYear && b.getMonth() > currentMonth))
                .map(b -> {
                    MonthlyBudgetResponseDto summary = mapMonthlyWithSpent(b, userId);
                    return mapper.map(summary, BudgetResponseDto.class);
                })
                .collect(Collectors.toList());

        BudgetListResponseDto dto = new BudgetListResponseDto();
        dto.setPast(past);
        dto.setPresent(present);
        dto.setUpcoming(upcoming);
        return dto;
    }

    @Override
    public BudgetListResponseDto getYearlyBudgetList() {
        int currentYear = LocalDate.now().getYear();
        UUID userId = userService.getAuthenticatedUser().getId();

        List<YearlyBudget> all = yearlyBudgetRepo.findAll();

        List<BudgetResponseDto> past = all.stream()
                .filter(b -> b.getYear() < currentYear)
                .map(b -> mapYearlyWithSpent(b, userId))
                .collect(Collectors.toList());

        BudgetResponseDto present = all.stream()
                .filter(b -> b.getYear() == currentYear)
                .findFirst()
                .map(b -> mapYearlyWithSpent(b, userId))
                .orElse(null);

        List<BudgetResponseDto> upcoming = all.stream()
                .filter(b -> b.getYear() > currentYear)
                .map(b -> mapYearlyWithSpent(b, userId))
                .collect(Collectors.toList());

        BudgetListResponseDto dto = new BudgetListResponseDto();
        dto.setPast(past);
        dto.setPresent(present);
        dto.setUpcoming(upcoming);
        return dto;
    }

    @Override
    public void deleteBudgetById(UUID budgetId) {
        Budget budget = budgetRepo.findById(budgetId)
                .orElseThrow(() -> new RuntimeException("Budget not found"));

        budgetRepo.delete(budget);
    }

    @Override
    public BudgetResponseDto updateMonthlyBudget(UUID budgetId, MonthlyBudgetRequestDto dto) {
        MonthlyBudget budget = (MonthlyBudget) budgetRepo.findById(budgetId)
                .orElseThrow(() -> new RuntimeException("Monthly budget not found"));

        budget.setYear(dto.getYear());
        budget.setMonth(dto.getMonth());
        budget.setTotalLimit(BigDecimal.valueOf(dto.getTotalLimit()));

        // clear old limits
        budget.getCategoryLimits().clear();

        // add updated category limits
        dto.getCategoryLimits().forEach(cl -> {
            Category category = categoryRepo.findById(cl.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            CategoryBudgetLimit limit = new CategoryBudgetLimit();
            limit.setBudget(budget);
            limit.setCategory(category);
            limit.setCategoryLimit(BigDecimal.valueOf(cl.getCategoryLimit()));
            budget.getCategoryLimits().add(limit);
        });

        MonthlyBudget saved = budgetRepo.save(budget);
        return mapper.map(saved, BudgetResponseDto.class);
    }

    @Override
    public BudgetResponseDto updateYearlyBudget(UUID budgetId, YearlyBudgetRequestDto dto) {
        YearlyBudget budget = (YearlyBudget) budgetRepo.findById(budgetId)
                .orElseThrow(() -> new RuntimeException("Yearly budget not found"));

        budget.setYear(dto.getYear());
        budget.setTotalLimit(BigDecimal.valueOf(dto.getTotalLimit()));

        budget.getCategoryLimits().clear();

        dto.getCategoryLimits().forEach(cl -> {
            Category category = categoryRepo.findById(cl.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            CategoryBudgetLimit limit = new CategoryBudgetLimit();
            limit.setBudget(budget);
            limit.setCategory(category);
            limit.setCategoryLimit(BigDecimal.valueOf(cl.getCategoryLimit()));
            budget.getCategoryLimits().add(limit);
        });

        YearlyBudget saved = budgetRepo.save(budget);
        return mapper.map(saved, BudgetResponseDto.class);
    }

    @Override
    public BudgetResponseDto getBudget(UUID budgetId) {
        return monthlyBudgetRepo.findById(budgetId)
                .map(b -> getMonthlyBudget(budgetId))
                .map(BudgetResponseDto.class::cast)
                .orElseGet(() -> yearlyBudgetRepo.findById(budgetId)
                        .map(b -> getYearlyBudget(budgetId))
                        .map(BudgetResponseDto.class::cast)
                        .orElseThrow(() -> new RuntimeException("Budget not found")));
    }


    public MonthlyBudgetBreakdownResponseDto getMonthlyBudget(UUID budgetId) {
        MonthlyBudget budget = monthlyBudgetRepo.findById(budgetId)
                .orElseThrow(() -> new RuntimeException("Monthly budget not found"));

        // total spent
        BigDecimal totalSpent = expenseTransactionRepo.sumSpentByUserAndYearAndMonth(
                budget.getUser().getId(),
                budget.getYear(),
                budget.getMonth()
        );

        // per-category spent
        List<CategorySpendResponseDto> categories = budget.getCategoryLimits().stream().map(cl -> {
            Double spent = expenseTransactionRepo.getMonthlySpentByCategory(
                    budget.getUser().getId(),
                    budget.getYear(),
                    budget.getMonth(),
                    cl.getCategory().getId()
            );
            return new CategorySpendResponseDto(
                    cl.getCategory().getId(),
                    cl.getCategory().getName(),
                    cl.getCategory().getColor(),
                    cl.getCategory().getIcon(),
                    cl.getCategoryLimit().doubleValue(),
                    spent != null ? spent : 0.0
            );
        }).collect(Collectors.toList());

        // response
        MonthlyBudgetBreakdownResponseDto dto = new MonthlyBudgetBreakdownResponseDto();
        dto.setId(budget.getId());
        dto.setYear(budget.getYear());
        dto.setMonth(budget.getMonth());
        dto.setTotalLimit(budget.getTotalLimit().doubleValue());
        dto.setTotalSpent(totalSpent != null ? totalSpent.doubleValue() : 0.0);
        dto.setCategories(categories);
        return dto;
    }

    public YearlyBudgetBreakdownResponseDto getYearlyBudget(UUID budgetId) {
        YearlyBudget budget = yearlyBudgetRepo.findById(budgetId)
                .orElseThrow(() -> new RuntimeException("Yearly budget not found"));

        // total spent
        BigDecimal totalSpent = expenseTransactionRepo.sumSpentByUserAndYear(
                budget.getUser().getId(),
                budget.getYear()
        );

        // per-category spent
        List<CategorySpendResponseDto> categories = budget.getCategoryLimits().stream().map(cl -> {
            Double spent = expenseTransactionRepo.getYearlySpentByCategory(
                    budget.getUser().getId(),
                    budget.getYear(),
                    cl.getCategory().getId()
            );
            return new CategorySpendResponseDto(
                    cl.getCategory().getId(),
                    cl.getCategory().getName(),
                    cl.getCategory().getColor(),
                    cl.getCategory().getIcon(),
                    cl.getCategoryLimit().doubleValue(),
                    spent != null ? spent : 0.0
            );
        }).collect(Collectors.toList());

        // response
        YearlyBudgetBreakdownResponseDto dto = new YearlyBudgetBreakdownResponseDto();
        dto.setId(budget.getId());
        dto.setYear(budget.getYear());
        dto.setTotalLimit(budget.getTotalLimit().doubleValue());
        dto.setTotalSpent(totalSpent != null ? totalSpent.doubleValue() : 0.0);
        dto.setCategories(categories);
        return dto;
    }
}