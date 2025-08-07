package com.expensetrace.app.model.transaction;

import com.expensetrace.app.enums.TransactionType;
import com.expensetrace.app.model.Tag;
import com.expensetrace.app.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "transaction")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "transaction_type", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue
    private UUID id;

    private int date;
    private int month;
    private int year;

    private LocalTime time;
    private BigDecimal amount;

    private TransactionType type;

    private String description;
}
