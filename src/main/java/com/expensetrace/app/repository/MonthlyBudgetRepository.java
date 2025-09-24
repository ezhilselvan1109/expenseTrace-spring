package com.expensetrace.app.repository;

import com.expensetrace.app.model.budget.MonthlyBudget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MonthlyBudgetRepository extends JpaRepository<MonthlyBudget, UUID> {
    List<MonthlyBudget> findAllByUserId(UUID userId);
    List<MonthlyBudget> findByUserId(UUID userId);
}
