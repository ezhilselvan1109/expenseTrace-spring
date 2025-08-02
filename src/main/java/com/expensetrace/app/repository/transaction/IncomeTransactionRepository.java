package com.expensetrace.app.repository.transaction;

import com.expensetrace.app.model.transaction.IncomeTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IncomeTransactionRepository extends JpaRepository<IncomeTransaction, UUID> {
}
