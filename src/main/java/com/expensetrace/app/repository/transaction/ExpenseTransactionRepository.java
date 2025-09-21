package com.expensetrace.app.repository.transaction;

import com.expensetrace.app.model.transaction.ExpenseTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ExpenseTransactionRepository extends JpaRepository<ExpenseTransaction, UUID> {
    List<ExpenseTransaction> findByAccountId(UUID accountId);

    List<ExpenseTransaction> findByUserIdAndTxnDate(UUID userId, LocalDate txnDate);

    // ---------- Monthly ----------
    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM ExpenseTransaction e " +
            "WHERE e.user.id = :userId " +
            "AND YEAR(e.txnDate) = :year " +
            "AND MONTH(e.txnDate) = :month")
    Double getMonthlySpent(@Param("userId") UUID userId,
                           @Param("year") int year,
                           @Param("month") int month);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM ExpenseTransaction e " +
            "WHERE e.user.id = :userId " +
            "AND YEAR(e.txnDate) = :year " +
            "AND MONTH(e.txnDate) = :month " +
            "AND e.category.id = :categoryId")
    Double getMonthlySpentByCategory(@Param("userId") UUID userId,
                                     @Param("year") int year,
                                     @Param("month") int month,
                                     @Param("categoryId") UUID categoryId);


    // ---------- Yearly ----------
    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM ExpenseTransaction e " +
            "WHERE e.user.id = :userId " +
            "AND YEAR(e.txnDate) = :year")
    Double getYearlySpent(@Param("userId") UUID userId,
                          @Param("year") int year);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM ExpenseTransaction e " +
            "WHERE e.user.id = :userId " +
            "AND YEAR(e.txnDate) = :year " +
            "AND e.category.id = :categoryId")
    Double getYearlySpentByCategory(@Param("userId") UUID userId,
                                    @Param("year") int year,
                                    @Param("categoryId") UUID categoryId);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM ExpenseTransaction t " +
            "WHERE (:userId IS NULL OR t.user.id = :userId) " +
            "AND (:startDate IS NULL OR t.txnDate >= :startDate) " +
            "AND (:endDate IS NULL OR t.txnDate <= :endDate)")
    BigDecimal getTotalExpense(@Param("userId") UUID userId,
                               @Param("startDate") LocalDate startDate,
                               @Param("endDate") LocalDate endDate);
}
