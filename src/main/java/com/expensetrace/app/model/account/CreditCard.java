package com.expensetrace.app.model.account;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("CREDIT_CARD")
@Getter
@Setter
@ToString(callSuper = true)
public class CreditCard extends Account {
    private BigDecimal currentAvailableLimit;
    private BigDecimal totalCreditLimit;
    private LocalDate billingCycleStartDate;
    private LocalDate billingCycleEndDate;
    private LocalDate paymentDueDate;
}