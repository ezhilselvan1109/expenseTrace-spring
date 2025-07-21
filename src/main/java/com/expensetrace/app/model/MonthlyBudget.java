package com.expensetrace.app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "monthly_budget")
@Getter
@Setter
public class MonthlyBudget extends Budget {

    private int month;

    @OneToMany(mappedBy = "budget", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CategoryBudgetLimit> categoryLimits;
}
