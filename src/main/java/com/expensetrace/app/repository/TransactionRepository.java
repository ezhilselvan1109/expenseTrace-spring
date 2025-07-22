package com.expensetrace.app.repository;

import com.expensetrace.app.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findByUserId(UUID userId);
    Page<Transaction> findByUserId(UUID userId, Pageable pageable);
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.user.id = :userId AND t.month = :month AND t.year = :year AND t.type = 'EXPENSE'")
    double sumAmountByUserIdAndMonthAndYear(UUID userId, int month, int year);
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.user.id = :userId AND t.year = :year AND t.type = 'EXPENSE'")
    double sumAmountByUserIdAndYear(UUID userId, int year);
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.category.id = :categoryId AND t.month = :month AND t.year = :year AND t.user.id = :userId AND t.type = 'EXPENSE'")
    double sumByCategoryAndMonthAndYear(UUID categoryId, int month, int year, UUID userId);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.category.id = :categoryId AND t.year = :year AND t.user.id = :userId AND t.type = 'EXPENSE'")
    double sumByCategoryAndYear(UUID categoryId, int year, UUID userId);

}
