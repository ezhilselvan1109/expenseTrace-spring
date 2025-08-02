package com.expensetrace.app.repository.transaction;

import com.expensetrace.app.model.transaction.AdjustmentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AdjustmentTransactionRepository extends JpaRepository<AdjustmentTransaction, UUID> {
}