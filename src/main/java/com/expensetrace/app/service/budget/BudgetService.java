package com.expensetrace.app.service.budget;

import com.expensetrace.app.exception.AccessDeniedException;
import com.expensetrace.app.model.*;
import com.expensetrace.app.repository.BudgetRepository;
import com.expensetrace.app.repository.CategoryRepository;
import com.expensetrace.app.repository.MonthlyBudgetRepository;
import com.expensetrace.app.repository.YearlyBudgetRepository;
import com.expensetrace.app.requestDto.CategoryLimitDto;
import com.expensetrace.app.requestDto.MonthlyBudgetRequestDto;
import com.expensetrace.app.requestDto.YearlyBudgetRequestDto;
import com.expensetrace.app.util.SecurityUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BudgetService implements IBudgetService {

    private final SecurityUtil securityUtil;
    private final MonthlyBudgetRepository monthlyBudgetRepo;
    private final YearlyBudgetRepository yearlyBudgetRepo;
    private final CategoryRepository categoryRepo;
    private final BudgetRepository budgetRepository;
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

}
