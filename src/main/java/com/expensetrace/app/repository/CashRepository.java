package com.expensetrace.app.repository;

import com.expensetrace.app.model.account.Cash;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;


public interface CashRepository extends JpaRepository<Cash, UUID> {

}

