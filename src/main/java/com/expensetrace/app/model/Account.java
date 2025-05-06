package com.expensetrace.app.model;

import com.expensetrace.app.enums.AccountType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class Account {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User user;

    private String name;

    @Enumerated(EnumType.STRING)
    private AccountType type;

    private BigDecimal currentBalance;

    // Credit card specific fields
    private BigDecimal availableCredit;
    private BigDecimal creditLimit;
    private LocalDate billingStart;
    private LocalDate dueDate;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<PaymentMode> paymentModes;
}
