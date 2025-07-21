package com.expensetrace.app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Entity
@Table(name = "category_budget_limit")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class CategoryBudgetLimit {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    private Budget budget;

    @ManyToOne
    private Category category;

    private double categoryLimit;
}
