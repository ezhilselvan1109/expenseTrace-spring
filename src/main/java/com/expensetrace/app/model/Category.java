package com.expensetrace.app.model;

import com.expensetrace.app.enums.CategoryType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "Category")
public class Category {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User user;

    private String name;

    @Enumerated(EnumType.STRING)
    private CategoryType type; // Enum: EXPENSE, INCOME

    private String color;

    private String icon;

    private boolean isDefault;
}
