package com.expensetrace.app.model.account;

import com.expensetrace.app.enums.PaymentModeType;
import com.expensetrace.app.model.account.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class PaymentMode {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    private Account account;

    private String name;

    @Enumerated(EnumType.STRING)
    private PaymentModeType type;
}

