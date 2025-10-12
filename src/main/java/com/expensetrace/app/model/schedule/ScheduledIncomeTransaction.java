package com.expensetrace.app.model.schedule;

import com.expensetrace.app.category.model.Category;
import com.expensetrace.app.account.model.Account;
import com.expensetrace.app.account.model.PaymentMode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "scheduled_income_transaction")
@Getter
@Setter
public class ScheduledIncomeTransaction extends ScheduledTransaction {

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
