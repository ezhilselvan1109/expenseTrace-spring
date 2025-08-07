package com.expensetrace.app.model.transaction;

import com.expensetrace.app.model.Category;
import com.expensetrace.app.model.Tag;
import com.expensetrace.app.model.User;
import com.expensetrace.app.model.account.Account;
import com.expensetrace.app.model.account.PaymentMode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "income_transaction")
@DiscriminatorValue("INCOME")
@Getter
@Setter
@NoArgsConstructor
public class IncomeTransaction extends Transaction {
    @ManyToOne
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "payment_mode_id")
    private PaymentMode paymentMode;

    @ManyToMany
    @JoinTable(
            name = "income_transaction_tags",
            joinColumns = @JoinColumn(name = "income_transaction_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;
}

