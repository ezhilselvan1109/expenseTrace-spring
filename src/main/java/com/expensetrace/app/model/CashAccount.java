package com.expensetrace.app.model;

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
public class CashAccount extends Account {

    private BigDecimal currentBalance;
}
