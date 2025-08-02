package com.expensetrace.app.model.transaction;

import com.expensetrace.app.model.Category;
import com.expensetrace.app.model.Tag;
import com.expensetrace.app.model.account.Account;
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
    private Category category;

    @ManyToOne
    private Account account;
}

