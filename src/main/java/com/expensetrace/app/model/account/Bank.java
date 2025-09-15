package com.expensetrace.app.model.account;

import jakarta.persistence.*;
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

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PaymentMode> paymentModes;
}