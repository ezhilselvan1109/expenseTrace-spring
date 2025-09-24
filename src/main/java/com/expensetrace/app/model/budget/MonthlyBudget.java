package com.expensetrace.app.model.budget;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
        name = "monthly_budgets",
        indexes = {
                @Index(name = "idx_monthly_user_year", columnList = "id, month")
        }
)
@Getter
@Setter
public class MonthlyBudget extends Budget {

    @Column(name = "month", nullable = false)
    private int month;
}