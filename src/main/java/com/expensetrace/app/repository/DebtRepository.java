package com.expensetrace.app.repository;

import com.expensetrace.app.enums.DebtType;
import com.expensetrace.app.model.debt.Debt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DebtRepository extends JpaRepository<Debt, UUID> {
    List<Debt> findByUserId(UUID userId);
    Page<Debt> findByUserId(UUID userId, Pageable pageable);
    Page<Debt> findByUserIdAndType(UUID userId, DebtType type, Pageable pageable);
}
