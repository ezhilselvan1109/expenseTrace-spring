package com.expensetrace.app.account.repository;

import com.expensetrace.app.account.enums.AccountType;
import com.expensetrace.app.account.model.Account;
import com.expensetrace.app.account.model.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;


public interface CreditCardRepository extends JpaRepository<CreditCard, UUID> {
    List<CreditCard> findByUserId(UUID userId);
    List<Account> findByUserIdAndType(UUID userId, AccountType type);
}

