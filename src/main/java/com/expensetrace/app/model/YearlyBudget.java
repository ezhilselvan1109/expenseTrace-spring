package com.expensetrace.app.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "yearly_budget")
@Getter
@Setter
public class YearlyBudget extends Budget {

    @OneToMany(mappedBy = "budget", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CategoryBudgetLimit> categoryLimits;
}
