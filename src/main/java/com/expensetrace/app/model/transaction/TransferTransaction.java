package com.expensetrace.app.model.transaction;

import com.expensetrace.app.model.Tag;
import com.expensetrace.app.model.account.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
@Entity
@Table(name = "transfer_transaction")
@DiscriminatorValue("TRANSFER")
@Getter
@Setter
@NoArgsConstructor
public class TransferTransaction extends Transaction {

    @ManyToOne
    @JoinColumn(name = "from_account_id")
    private Account fromAccount;

    @ManyToOne
    @JoinColumn(name = "to_account_id")
    private Account toAccount;
}

