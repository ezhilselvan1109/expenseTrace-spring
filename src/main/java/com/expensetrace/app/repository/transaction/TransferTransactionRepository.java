package com.expensetrace.app.repository.transaction;

import com.expensetrace.app.model.transaction.TransferTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransferTransactionRepository extends JpaRepository<TransferTransaction, UUID> {
}