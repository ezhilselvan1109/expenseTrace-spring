package com.expensetrace.app.repository.schedule;

import com.expensetrace.app.enums.schedule.ExecutionStatus;
import com.expensetrace.app.model.schedule.ScheduledTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ScheduledTransactionRepository extends JpaRepository<ScheduledTransaction, UUID> {
    Page<ScheduledTransaction> findByStatus(ExecutionStatus status, Pageable pageable);
}
