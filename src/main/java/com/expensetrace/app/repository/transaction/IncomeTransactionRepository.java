package com.expensetrace.app.repository.transaction;

import com.expensetrace.app.model.transaction.IncomeTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface IncomeTransactionRepository extends JpaRepository<IncomeTransaction, UUID> {
    List<IncomeTransaction> findByAccountId(UUID accountId);

    List<IncomeTransaction> findByUserIdAndTxnDate(UUID userId, LocalDate txnDate);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM IncomeTransaction t " +
            "WHERE (:userId IS NULL OR t.user.id = :userId) " +
            "AND (:startDate IS NULL OR t.txnDate >= :startDate) " +
            "AND (:endDate IS NULL OR t.txnDate <= :endDate)")
    BigDecimal getTotalIncome(@Param("userId") UUID userId,
                              @Param("startDate") LocalDate startDate,
                              @Param("endDate") LocalDate endDate);
}
