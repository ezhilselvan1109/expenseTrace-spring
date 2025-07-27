package com.expensetrace.app.model.budget;

import com.expensetrace.app.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
public abstract class Budget {

    @Id
    private UUID id;

    private int year;

    private double totalLimit;

    @ManyToOne
    private User user;
}
