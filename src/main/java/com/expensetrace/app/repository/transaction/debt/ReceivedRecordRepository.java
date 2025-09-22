package com.expensetrace.app.repository.transaction.debt;

import com.expensetrace.app.model.transaction.record.ReceivedRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface ReceivedRecordRepository extends JpaRepository<ReceivedRecord, UUID> {
    List<ReceivedRecord> findByAccountId(UUID accountId);
    @Query("SELECT COALESCE(SUM(r.amount), 0) FROM ReceivedRecord r WHERE r.debt.id = :debtId")
    BigDecimal getTotalReceivedByDebt(UUID debtId);
}