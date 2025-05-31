package com.expensetrace.app.repository;

import com.expensetrace.app.enums.RecordType;
import com.expensetrace.app.model.Record;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RecordsRepository extends JpaRepository<Record, UUID> {
    List<Record> findByDebtId(UUID debtId);

    Page<Record> findByDebtId(UUID debtId, Pageable pageable);

    Page<Record> findByDebtIdAndType(UUID debtId, RecordType type, Pageable pageable);
}
