package com.expensetrace.app.repository.transaction;

import com.expensetrace.app.model.transaction.ExpenseTransaction;
import com.expensetrace.app.model.transaction.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ExpenseTransactionRepository extends JpaRepository<ExpenseTransaction, UUID> {
    Page<Transaction> findAllByUserId(UUID userId, Pageable pageable);
}