package com.expensetrace.app.repository.account;

import com.expensetrace.app.enums.AccountType;
import com.expensetrace.app.model.account.Account;
import com.expensetrace.app.model.account.Cash;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;


public interface CashRepository extends JpaRepository<Cash, UUID> {
    List<Account> findByUserIdAndType(UUID userId, AccountType type);
}

