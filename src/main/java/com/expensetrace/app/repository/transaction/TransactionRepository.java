package com.expensetrace.app.repository.transaction;

import com.expensetrace.app.model.transaction.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findByUserId(UUID userId);
    Page<Transaction> findByUserId(UUID userId, Pageable pageable);
    List<Transaction> findByTags_Id(UUID tagId);
    int countByTags_Id(UUID tagId);
    Page<Transaction> findAllByUserId(UUID userId, Pageable pageable);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.user.id = :userId AND t.year = :year")
    double sumAmountByUserIdAndYear(@Param("userId") UUID userId,
                                    @Param("year") int year);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.user.id = :userId AND t.month = :month AND t.year = :year")
    double sumAmountByUserIdAndMonthAndYear(@Param("userId") UUID userId,
                                            @Param("month") int month,
                                            @Param("year") int year);

}
