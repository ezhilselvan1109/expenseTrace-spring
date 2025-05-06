package com.expensetrace.app.model;
import com.expensetrace.app.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class Transaction {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne private User user;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private LocalDate date;
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

    @OneToOne(mappedBy = "transaction", cascade = CascadeType.ALL)
    private Attachment attachment;
}
