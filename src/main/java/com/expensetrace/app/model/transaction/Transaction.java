package com.expensetrace.app.model.transaction;

import com.expensetrace.app.enums.TransactionType;
import com.expensetrace.app.model.Category;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(
        name = "transactions"
)
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@DiscriminatorColumn(name = "transaction_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Transaction {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "txn_date", nullable = false)
    private LocalDate txnDate;

    @Column(name = "txn_time", nullable = false)
    private LocalTime txnTime;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", insertable = false, updatable = false)
    private TransactionType type;
}
