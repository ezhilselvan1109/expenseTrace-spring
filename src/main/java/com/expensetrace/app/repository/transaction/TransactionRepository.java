package com.expensetrace.app.repository.transaction;

import com.expensetrace.app.dto.response.summary.MonthSummaryResponseDto;
import com.expensetrace.app.model.transaction.ExpenseTransaction;
import com.expensetrace.app.model.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findByTxnDateBetween(LocalDate from, LocalDate to);

    @Query("""
        SELECT new com.expensetrace.app.dto.response.summary.MonthSummaryResponseDto(
            COALESCE(SUM(CASE 
                WHEN t.type = com.expensetrace.app.enums.TransactionType.EXPENSE 
                  OR t.type = com.expensetrace.app.enums.TransactionType.DEBT_PAID 
                THEN t.amount ELSE 0 END), 0),
            COALESCE(SUM(CASE 
                WHEN t.type = com.expensetrace.app.enums.TransactionType.INCOME 
                  OR t.type = com.expensetrace.app.enums.TransactionType.DEBT_RECEIVED 
                THEN t.amount ELSE 0 END), 0),
            DAY(t.txnDate)
        )
        FROM Transaction t
        WHERE YEAR(t.txnDate) = :year
          AND MONTH(t.txnDate) = :month
        GROUP BY t.txnDate
        ORDER BY t.txnDate
    """)
    List<MonthSummaryResponseDto> findMonthlySummary(@Param("year") int year, @Param("month") int month);

}
