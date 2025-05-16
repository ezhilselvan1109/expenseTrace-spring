package com.expensetrace.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class Tag {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    @ManyToOne
    private User user; // optional, if tags are user-owned
}
