package com.expensetrace.app.model;

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
