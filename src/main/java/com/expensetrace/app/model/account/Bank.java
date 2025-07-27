package com.expensetrace.app.model.account;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Entity
@DiscriminatorValue("BANK")
@Getter
@Setter
@ToString(callSuper = true)
public class Bank extends Account {

    private BigDecimal currentBalance;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<PaymentMode> paymentModes;
}
