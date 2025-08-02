package com.expensetrace.app.model.debt;

import com.expensetrace.app.enums.RecordType;
import com.expensetrace.app.model.account.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "Record")
public class Record {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "debt_id")
    private Debt debt;


    private LocalDate date;

    private LocalTime time;

    private BigDecimal amount;

    private String description;

    @ManyToOne private Account account;

    @Enumerated(EnumType.STRING)
    private RecordType type;
}