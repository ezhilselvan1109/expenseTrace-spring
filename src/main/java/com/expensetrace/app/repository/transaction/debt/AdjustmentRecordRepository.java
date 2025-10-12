package com.expensetrace.app.repository.transaction.debt;

import com.expensetrace.app.model.transaction.record.AdjustmentRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AdjustmentRecordRepository extends JpaRepository<AdjustmentRecord, UUID> {
    List<AdjustmentRecord> findAllByDebtId(UUID debtId);
}