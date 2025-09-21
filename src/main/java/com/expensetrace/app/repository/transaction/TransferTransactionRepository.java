package com.expensetrace.app.repository.transaction;

import com.expensetrace.app.model.transaction.TransferTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TransferTransactionRepository extends JpaRepository<TransferTransaction, UUID> {
    List<TransferTransaction> findByFromAccountIdOrToAccountId(UUID fromId, UUID toId);
    List<TransferTransaction> findByFromAccountId(UUID fromId);
    List<TransferTransaction> findByToAccountId(UUID toId);

    List<TransferTransaction> findByUserIdAndTxnDate(UUID userId, LocalDate txnDate);
}
