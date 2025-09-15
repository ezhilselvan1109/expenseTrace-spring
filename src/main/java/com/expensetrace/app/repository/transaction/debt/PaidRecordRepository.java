package com.expensetrace.app.repository.transaction.debt;

import com.expensetrace.app.model.transaction.record.PaidRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PaidRecordRepository extends JpaRepository<PaidRecord, UUID> {
    List<PaidRecord> findByAccountId(UUID accountId);
}
