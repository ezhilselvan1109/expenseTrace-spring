package com.expensetrace.app.model.transaction.record;

import com.expensetrace.app.model.account.Account;
import com.expensetrace.app.model.account.PaymentMode;
import com.expensetrace.app.model.debt.Debt;
import com.expensetrace.app.model.transaction.Transaction;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "paid_Record")
@DiscriminatorValue("PAID")
@Getter
@Setter
@NoArgsConstructor
public class PaidRecord extends Transaction {
    @ManyToOne
    @JoinColumn(name = "debt_id")
    private Debt debt;

    @ManyToOne private Account account;

    @ManyToOne private PaymentMode paymentMode;
}
