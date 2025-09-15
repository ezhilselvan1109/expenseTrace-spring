package com.expensetrace.app.repository.schedule;

import com.expensetrace.app.model.schedule.ScheduledExpenseTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ScheduledExpenseTransactionRepository extends JpaRepository<ScheduledExpenseTransaction, UUID> {}
