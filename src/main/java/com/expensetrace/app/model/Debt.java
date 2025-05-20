package com.expensetrace.app.model;

import com.expensetrace.app.enums.DebtsType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "Debt")
public class Debt {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne private User user;

    private String personName;

    private LocalDate dueDate;

    private String additionalDetail;

    @Enumerated(EnumType.STRING)
    private DebtsType type;
}
