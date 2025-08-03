package com.expensetrace.app.model.transaction;

import com.expensetrace.app.model.account.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Table(name = "adjustment_transaction")
@DiscriminatorValue("ADJUSTMENT")
@Getter
@Setter
@NoArgsConstructor
public class AdjustmentTransaction extends Transaction {

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}

