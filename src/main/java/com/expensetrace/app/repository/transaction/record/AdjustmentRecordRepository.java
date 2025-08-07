package com.expensetrace.app.repository.transaction.record;

import com.expensetrace.app.enums.TransactionType;
import com.expensetrace.app.model.transaction.Transaction;
import com.expensetrace.app.model.transaction.record.AdjustmentRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AdjustmentRecordRepository extends JpaRepository<AdjustmentRecord, UUID> {
    Page<Transaction> findByDebtIdAndType(UUID userId, TransactionType type, Pageable pageable);
}
