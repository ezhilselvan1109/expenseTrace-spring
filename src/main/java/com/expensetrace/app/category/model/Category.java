package com.expensetrace.app.category.model;

import com.expensetrace.app.enums.CategoryType;
import com.expensetrace.app.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(
        name = "categories",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "name"})
)
public class Category {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String name;

    @Enumerated(EnumType.STRING)
    private CategoryType type;

    private String color;

    private String icon;

    private boolean isDefault;

    private boolean isDeletable = false;
}