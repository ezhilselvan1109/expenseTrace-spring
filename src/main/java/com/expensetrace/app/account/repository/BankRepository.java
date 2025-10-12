package com.expensetrace.app.account.repository;

import com.expensetrace.app.account.enums.AccountType;
import com.expensetrace.app.account.model.Account;
import com.expensetrace.app.account.model.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;


public interface BankRepository extends JpaRepository<Bank, UUID> {
    List<Account> findByUserIdAndType(UUID userId, AccountType type);
}

