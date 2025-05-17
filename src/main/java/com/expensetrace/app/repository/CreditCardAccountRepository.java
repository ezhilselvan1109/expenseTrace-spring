package com.expensetrace.app.repository;

import com.expensetrace.app.model.CreditCardAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;


public interface CreditCardAccountRepository extends JpaRepository<CreditCardAccount, UUID> {
}

