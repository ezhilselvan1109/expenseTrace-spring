package com.expensetrace.app.model;

import com.expensetrace.app.enums.BudgetType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
@Entity
@Table(name = "budget")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Budget {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    private User user;

    @Enumerated(EnumType.STRING)
    private BudgetType budgetType;

    private int year;

    private Integer month;

    private double totalLimit;

    @OneToMany(mappedBy = "budget", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<CategoryBudgetLimit> categoryLimits = new HashSet<>();
}
