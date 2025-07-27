package com.expensetrace.app.repository;

import com.expensetrace.app.model.budget.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BudgetRepository extends JpaRepository<Budget, UUID> {
}
