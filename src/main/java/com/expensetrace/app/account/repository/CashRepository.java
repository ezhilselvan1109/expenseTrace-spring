package com.expensetrace.app.account.repository;

import com.expensetrace.app.account.enums.AccountType;
import com.expensetrace.app.account.model.Account;
import com.expensetrace.app.account.model.Cash;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;


public interface CashRepository extends JpaRepository<Cash, UUID> {
    List<Account> findByUserIdAndType(UUID userId, AccountType type);
}

