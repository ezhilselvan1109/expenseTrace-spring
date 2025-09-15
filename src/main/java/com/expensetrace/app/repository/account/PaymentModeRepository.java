package com.expensetrace.app.repository.account;

import com.expensetrace.app.enums.AccountType;
import com.expensetrace.app.model.account.Account;
import com.expensetrace.app.model.account.PaymentMode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PaymentModeRepository extends JpaRepository<PaymentMode, UUID> {
    boolean existsByName(String name);
    List<PaymentMode> findByAccountId(UUID accountId);
}

