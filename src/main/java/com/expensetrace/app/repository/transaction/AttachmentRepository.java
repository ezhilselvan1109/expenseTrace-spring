package com.expensetrace.app.repository.transaction;

import com.expensetrace.app.model.transaction.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AttachmentRepository extends JpaRepository<Attachment, UUID> {
}
