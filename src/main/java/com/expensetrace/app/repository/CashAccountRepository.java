package com.expensetrace.app.repository;

import com.expensetrace.app.model.CashAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;


public interface CashAccountRepository extends JpaRepository<CashAccount, UUID> {

}

