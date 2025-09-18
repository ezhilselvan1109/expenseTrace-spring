package com.expensetrace.app.repository.transaction;

import com.expensetrace.app.model.transaction.ExpenseTransaction;
import com.expensetrace.app.model.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findByTxnDateBetween(LocalDate from, LocalDate to);
}
