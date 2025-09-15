package com.expensetrace.app.repository.schedule;

import com.expensetrace.app.model.schedule.ScheduledTransferTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ScheduledTransferTransactionRepository extends JpaRepository<ScheduledTransferTransaction, UUID> {}
