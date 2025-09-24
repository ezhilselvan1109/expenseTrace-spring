package com.expensetrace.app.model.budget;

import com.expensetrace.app.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.BatchSize;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "budgets",
        indexes = {
                @Index(name = "idx_budget_user_year", columnList = "user_id, budget_year")
        })
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@ToString
public abstract class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "budget_year", nullable = false)
    private int year;

    @Column(name = "total_limit", nullable = false, precision = 19, scale = 2)
    private BigDecimal totalLimit;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private User user;

    @OneToMany(mappedBy = "budget", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @BatchSize(size = 20)
    @ToString.Exclude
    private Set<CategoryBudgetLimit> categoryLimits;
}