package com.expensetrace.app.model.budget;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "yearly_budgets")
@Getter
@Setter
public class YearlyBudget extends Budget {
}