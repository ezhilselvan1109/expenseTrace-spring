package com.expensetrace.app.model.account;

import com.expensetrace.app.enums.PaymentModeType;
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
@Table(name = "payment_modes")
public class PaymentMode {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    private String name;

    @Enumerated(EnumType.STRING)
    private PaymentModeType type;
}