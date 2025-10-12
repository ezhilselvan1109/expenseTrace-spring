package com.expensetrace.app.account.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@DiscriminatorValue("CASH")
@Getter
@Setter
@ToString(callSuper = true)
public class Cash extends Account {
}
