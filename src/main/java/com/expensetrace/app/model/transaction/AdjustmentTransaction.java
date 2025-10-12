package com.expensetrace.app.model.transaction;

import com.expensetrace.app.model.User;
import com.expensetrace.app.account.model.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "adjustment_transactions")
@Getter
@Setter
@NoArgsConstructor
@DiscriminatorValue("ADJUSTMENT")
public class AdjustmentTransaction extends Transaction{

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}

