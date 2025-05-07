package com.expensetrace.app.repository;

import com.expensetrace.app.model.PaymentMode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentModeRepository extends JpaRepository<PaymentMode, Long> {
    boolean existsByName(String name);
    List<PaymentMode> findByAccountId(Long accountId);

}

