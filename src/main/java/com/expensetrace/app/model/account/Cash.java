package com.expensetrace.app.model.account;

import com.expensetrace.app.model.account.Account;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Entity
@DiscriminatorValue("CASH")
@Getter
@Setter
@ToString(callSuper = true)
public class Cash extends Account {

    private BigDecimal currentBalance;
}
