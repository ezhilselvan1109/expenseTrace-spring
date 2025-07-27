package com.expensetrace.app.model.transaction;
import com.expensetrace.app.enums.TransactionType;
import com.expensetrace.app.model.Category;
import com.expensetrace.app.model.Tag;
import com.expensetrace.app.model.User;
import com.expensetrace.app.model.account.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class Transaction {
    @Id @GeneratedValue
    private UUID id;

    @ManyToOne private User user;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private int date;
    private int month;
    private int year;
    private LocalTime time;
    private BigDecimal amount;

    @ManyToOne private Category category;
    @ManyToOne private Account account;

    private String description;

    @ManyToMany
    @JoinTable(
            name = "transaction_tags",
            joinColumns = @JoinColumn(name = "transaction_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;
}
