package com.expensetrace.app.model.debt;

import com.expensetrace.app.enums.DebtType;
import com.expensetrace.app.model.User;
import com.expensetrace.app.model.transaction.record.AdjustmentRecord;
import com.expensetrace.app.model.transaction.record.DebtRecord;
import com.expensetrace.app.model.transaction.record.PaidRecord;
import com.expensetrace.app.model.transaction.record.ReceivedRecord;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "debts", indexes = {
        @Index(name = "idx_user_type", columnList = "user_id, type"),
        @Index(name = "idx_due_date", columnList = "dueDate")
})
public class Debt {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String personName;

    private LocalDate dueDate;

    private String additionalDetail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DebtType type;

    private BigDecimal totalAmount;
    @OneToMany(mappedBy = "debt", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DebtRecord> records = new ArrayList<>();
}
