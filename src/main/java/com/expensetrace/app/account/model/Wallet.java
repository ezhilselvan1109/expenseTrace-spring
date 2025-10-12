package com.expensetrace.app.account.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@DiscriminatorValue("WALLET")
@Getter
@Setter
@ToString(callSuper = true)
public class Wallet extends Account {
}
