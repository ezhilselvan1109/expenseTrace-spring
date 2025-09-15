package com.expensetrace.app.model;

import com.expensetrace.app.model.transaction.TaggableTransaction;
import com.expensetrace.app.model.transaction.Transaction;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString
public class Tag {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "tags", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<TaggableTransaction> transactions = new HashSet<>();

    public int getTransactionsCount() {
        return transactions != null ? transactions.size() : 0;
    }
}
