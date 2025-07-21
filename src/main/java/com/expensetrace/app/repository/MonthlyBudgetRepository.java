package com.expensetrace.app.repository;

import com.expensetrace.app.model.MonthlyBudget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MonthlyBudgetRepository extends JpaRepository<MonthlyBudget, UUID> {}
