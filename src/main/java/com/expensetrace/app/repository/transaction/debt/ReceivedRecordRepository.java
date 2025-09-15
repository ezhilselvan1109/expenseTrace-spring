package com.expensetrace.app.repository.transaction.debt;

import com.expensetrace.app.model.transaction.record.ReceivedRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReceivedRecordRepository extends JpaRepository<ReceivedRecord, UUID> {
    List<ReceivedRecord> findByAccountId(UUID accountId);
}