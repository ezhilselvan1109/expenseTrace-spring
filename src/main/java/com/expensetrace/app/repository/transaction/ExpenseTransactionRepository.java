package com.expensetrace.app.repository.transaction;

import com.expensetrace.app.model.transaction.ExpenseTransaction;
import com.expensetrace.app.model.transaction.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ExpenseTransactionRepository extends JpaRepository<ExpenseTransaction, UUID> {
    Page<Transaction> findAllByUserId(UUID userId, Pageable pageable);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM ExpenseTransaction e WHERE e.user.id = :userId AND e.category.id = :categoryId AND e.month = :month AND e.year = :year")
    double sumByCategoryAndMonthAndYear(@Param("categoryId") UUID categoryId,
                                        @Param("month") int month,
                                        @Param("year") int year,
                                        @Param("userId") UUID userId);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM ExpenseTransaction e WHERE e.user.id = :userId AND e.category.id = :categoryId AND e.year = :year")
    double sumByCategoryAndYear(@Param("categoryId") UUID categoryId,
                                @Param("year") int year,
                                @Param("userId") UUID userId);


}