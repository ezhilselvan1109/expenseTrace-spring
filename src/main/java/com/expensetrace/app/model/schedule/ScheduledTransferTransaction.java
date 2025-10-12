package com.expensetrace.app.model.schedule;

import com.expensetrace.app.category.model.Category;
import com.expensetrace.app.account.model.Account;
import com.expensetrace.app.account.model.PaymentMode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "scheduled_transfer_transaction")
@Getter
@Setter
public class ScheduledTransferTransaction extends ScheduledTransaction {

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "from_account_id")
    private Account fromAccount;

    @ManyToOne
    @JoinColumn(name = "from_payment_mode_id")
    private PaymentMode fromPaymentMode;

    @ManyToOne
    @JoinColumn(name = "to_account_id")
    private Account toAccount;

    @ManyToOne
    @JoinColumn(name = "to_payment_mode_id")
    private PaymentMode toPaymentMode;
}
