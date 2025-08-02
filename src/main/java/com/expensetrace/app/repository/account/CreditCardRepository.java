package com.expensetrace.app.repository.account;

import com.expensetrace.app.model.account.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;


public interface CreditCardRepository extends JpaRepository<CreditCard, UUID> {
    List<CreditCard> findByUserId(UUID userId);
}

