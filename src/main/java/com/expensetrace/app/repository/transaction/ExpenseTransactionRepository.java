package com.expensetrace.app.repository.transaction;

import com.expensetrace.app.model.transaction.ExpenseTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ExpenseTransactionRepository extends JpaRepository<ExpenseTransaction, UUID> {
}