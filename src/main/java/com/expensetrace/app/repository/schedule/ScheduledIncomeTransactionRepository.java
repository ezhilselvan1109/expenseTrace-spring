package com.expensetrace.app.repository.schedule;

import com.expensetrace.app.model.schedule.ScheduledIncomeTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ScheduledIncomeTransactionRepository extends JpaRepository<ScheduledIncomeTransaction, UUID> {}

