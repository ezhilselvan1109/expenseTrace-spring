package com.expensetrace.app.model.transaction.record;

import com.expensetrace.app.account.model.Account;
import com.expensetrace.app.account.model.PaymentMode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "paid_records")
@Getter @Setter @NoArgsConstructor
@DiscriminatorValue("DEBT_PAID")
public class PaidRecord extends DebtRecord {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_mode_id")
    private PaymentMode paymentMode;
}