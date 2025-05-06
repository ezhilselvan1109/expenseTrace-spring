package com.expensetrace.app.responseDto;

import com.expensetrace.app.enums.TransactionType;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class TransactionResponseDTO {
    private Long id;
    private Long userId;
    private TransactionType type;
    private LocalDate date;
    private LocalTime time;
    private BigDecimal amount;
    private Long categoryId;
    private Long accountId;
    private String description;
    private List<Long> tagIds;
    private MultipartFile attachment; // for upload
}

