package com.expensetrace.app.model.transaction;

import com.expensetrace.app.model.Category;
import com.expensetrace.app.model.account.Account;
import com.expensetrace.app.model.account.PaymentMode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "expense_transaction")
@DiscriminatorValue("EXPENSE")
@Getter
@Setter
@NoArgsConstructor
public class ExpenseTransaction extends Transaction {

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "payment_mode_id")
    private PaymentMode paymentMode;
}
