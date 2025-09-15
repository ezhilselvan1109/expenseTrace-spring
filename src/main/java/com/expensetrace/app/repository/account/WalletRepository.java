package com.expensetrace.app.repository.account;

import com.expensetrace.app.enums.AccountType;
import com.expensetrace.app.model.account.Account;
import com.expensetrace.app.model.account.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;


public interface WalletRepository extends JpaRepository<Wallet, UUID> {
    List<Account> findByUserIdAndType(UUID userId, AccountType type);
}

