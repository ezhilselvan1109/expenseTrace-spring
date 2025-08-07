package com.expensetrace.app.repository.transaction;

import com.expensetrace.app.enums.TransactionType;
import com.expensetrace.app.model.transaction.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    @Query(value = "SELECT * FROM transaction t WHERE t.transaction_type IN ('PAID', 'RECEIVED','DEBT_ADJUSTMENT') AND t.debt_id = :debtId ORDER BY t.time DESC",
            countQuery = "SELECT COUNT(*) FROM transaction t WHERE t.transaction_type IN ('PAID', 'RECEIVED','DEBT_ADJUSTMENT') AND t.debt_id = :debtId",
            nativeQuery = true)
    Page<Transaction> findAllRecordByDebtId(UUID debtId, Pageable pageable);

    @Query(value = "SELECT t FROM Transaction t " +
            "WHERE (TYPE(t) = ExpenseTransaction AND TREAT(t AS ExpenseTransaction).user.id = :userId) " +
            "OR (TYPE(t) = IncomeTransaction AND TREAT(t AS IncomeTransaction).user.id = :userId) " +
            "OR (TYPE(t) = TransferTransaction AND TREAT(t AS TransferTransaction).user.id = :userId) " +
            "OR (TYPE(t) = AdjustmentTransaction AND TREAT(t AS AdjustmentTransaction).user.id = :userId)",
            countQuery = "SELECT COUNT(t) FROM Transaction t " +
                    "WHERE (TYPE(t) = ExpenseTransaction AND TREAT(t AS ExpenseTransaction).user.id = :userId) " +
                    "OR (TYPE(t) = IncomeTransaction AND TREAT(t AS IncomeTransaction).user.id = :userId) " +
                    "OR (TYPE(t) = TransferTransaction AND TREAT(t AS TransferTransaction).user.id = :userId) " +
                    "OR (TYPE(t) = AdjustmentTransaction AND TREAT(t AS AdjustmentTransaction).user.id = :userId)"
    )
    Page<Transaction> findAllByUserId(@Param("userId") UUID userId, Pageable pageable);

    /*@Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.user.id = :userId AND t.year = :year")
    double sumAmountByUserIdAndYear(@Param("userId") UUID userId,
                                    @Param("year") int year);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.user.id = :userId AND t.month = :month AND t.year = :year")
    double sumAmountByUserIdAndMonthAndYear(@Param("userId") UUID userId,
                                            @Param("month") int month,
                                            @Param("year") int year);*/
}
