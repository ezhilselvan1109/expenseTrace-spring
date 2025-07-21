package com.expensetrace.app.repository;

import com.expensetrace.app.model.YearlyBudget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface YearlyBudgetRepository extends JpaRepository<YearlyBudget, UUID> {}
