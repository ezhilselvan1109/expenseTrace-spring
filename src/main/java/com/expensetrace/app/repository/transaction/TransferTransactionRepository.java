package com.expensetrace.app.repository.transaction;

import com.expensetrace.app.model.transaction.ExpenseTransaction;
import com.expensetrace.app.model.transaction.Transaction;
import com.expensetrace.app.model.transaction.TransferTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransferTransactionRepository extends JpaRepository<TransferTransaction, UUID> {
    Page<Transaction> findAllByUserId(UUID userId, Pageable pageable);

    List<TransferTransaction> findByTags_Id(UUID tagId);

    int countByTags_Id(UUID tagId);
}