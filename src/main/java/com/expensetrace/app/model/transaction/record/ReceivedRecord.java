package com.expensetrace.app.model.transaction.record;

import com.expensetrace.app.account.model.Account;
import com.expensetrace.app.account.model.PaymentMode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "received_records")
@Getter @Setter @NoArgsConstructor
@DiscriminatorValue("DEBT_RECEIVED")
public class ReceivedRecord extends DebtRecord {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_mode_id")
    private PaymentMode paymentMode;
}

