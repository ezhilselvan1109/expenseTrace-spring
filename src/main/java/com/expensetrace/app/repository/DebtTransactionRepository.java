package com.expensetrace.app.repository;

import com.expensetrace.app.enums.DebtTransactionType;
import com.expensetrace.app.model.DebtTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DebtTransactionRepository extends JpaRepository<DebtTransaction, UUID> {
    List<DebtTransaction> findByDebtId(UUID debtId);

    Page<DebtTransaction> findByDebtId(UUID debtId, Pageable pageable);

    Page<DebtTransaction> findByDebtIdAndType(UUID debtId, DebtTransactionType type, Pageable pageable);
}
