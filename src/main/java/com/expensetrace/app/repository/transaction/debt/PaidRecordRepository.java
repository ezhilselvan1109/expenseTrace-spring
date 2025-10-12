package com.expensetrace.app.repository.transaction.debt;

import com.expensetrace.app.model.transaction.record.PaidRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface PaidRecordRepository extends JpaRepository<PaidRecord, UUID> {
    List<PaidRecord> findByAccountId(UUID accountId);

    List<PaidRecord> findAllByDebtId(UUID debtId);
    @Query("SELECT COALESCE(SUM(r.amount), 0) FROM PaidRecord r WHERE r.debt.id = :debtId")
    BigDecimal getTotalPaidByDebt(UUID debtId);
}
