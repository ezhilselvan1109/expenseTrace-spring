package com.expensetrace.app.model;

import com.expensetrace.app.model.schedule.ScheduledTransaction;
import com.expensetrace.app.model.transaction.TaggableTransaction;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(
        name = "tags",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "name"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Tag {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany(mappedBy = "tags")
    private Set<TaggableTransaction> transactions = new HashSet<>();

    @ManyToMany(mappedBy = "tags")
    private Set<ScheduledTransaction> scheduledTransactions = new HashSet<>();

    public int getTransactionsCount() {
        return transactions != null ? transactions.size() : 0;
    }

    public int getScheduledTransactionsCount() {
        return scheduledTransactions != null ? scheduledTransactions.size() : 0;
    }
}