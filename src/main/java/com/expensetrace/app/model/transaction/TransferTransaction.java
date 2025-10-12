package com.expensetrace.app.model.transaction;

import com.expensetrace.app.model.User;
import com.expensetrace.app.account.model.Account;
import com.expensetrace.app.account.model.PaymentMode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "transfer_transactions")
@DiscriminatorValue("TRANSFER")
@Getter
@Setter
@NoArgsConstructor
public class TransferTransaction extends TaggableTransaction {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_account_id", nullable = false)
    private Account fromAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_account_id", nullable = false)
    private Account toAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_payment_mode_id")
    private PaymentMode fromPaymentMode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_payment_mode_id")
    private PaymentMode toPaymentMode;
}
