package com.expensetrace.app.model.schedule;

import com.expensetrace.app.model.transaction.Transaction;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "scheduled_transaction_occurrence")
@Getter
@Setter
public class ScheduledTransactionOccurrence {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "scheduled_id", nullable = false)
    private ScheduledTransaction scheduledTransaction;

    @ManyToOne
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;
}

