package com.expensetrace.app.requestDto;

import com.expensetrace.app.enums.TransactionType;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
public class TransactionRequestDTO {
    private TransactionType type;
    private LocalDate date;
    private LocalTime time;
    private BigDecimal amount;
    private UUID categoryId;
    private UUID accountId;
    private String description;
    private List<UUID> tagIds;
}

