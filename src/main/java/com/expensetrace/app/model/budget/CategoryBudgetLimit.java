package com.expensetrace.app.model.budget;

import com.expensetrace.app.model.Category;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(
        name = "category_budget_limits",
        uniqueConstraints = @UniqueConstraint(columnNames = {"budget_id", "category_id"}),
        indexes = {
                @Index(name = "idx_cbl_budget", columnList = "budget_id"),
                @Index(name = "idx_cbl_category", columnList = "category_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@ToString
public class CategoryBudgetLimit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "budget_id", nullable = false)
    @ToString.Exclude
    private Budget budget;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @ToString.Exclude
    private Category category;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal categoryLimit;

    @BatchSize(size = 20)
    public Budget getBudget() {
        return budget;
    }
}