package com.expensetrace.app.service;

import com.expensetrace.app.dto.response.summary.MonthSummaryResponseDto;
import com.expensetrace.app.repository.transaction.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SummaryService {

    private final TransactionRepository transactionRepository;

    public List<MonthSummaryResponseDto> getMonthlySummary(int year, int month) {
        return transactionRepository.findMonthlySummary(year, month);
    }
}

