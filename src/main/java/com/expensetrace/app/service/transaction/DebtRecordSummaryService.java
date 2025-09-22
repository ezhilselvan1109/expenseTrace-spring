package com.expensetrace.app.service.transaction;

import com.expensetrace.app.dto.response.debt.DebtRecordSummaryResponseDto;
import com.expensetrace.app.model.debt.Debt;
import com.expensetrace.app.repository.DebtRepository;
import com.expensetrace.app.repository.transaction.debt.PaidRecordRepository;
import com.expensetrace.app.repository.transaction.debt.ReceivedRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DebtRecordSummaryService {

    private final PaidRecordRepository paidRepo;
    private final ReceivedRecordRepository receivedRepo;
    private final DebtRepository debtRepo;

    public DebtRecordSummaryResponseDto getSummary(UUID debtId) {
        Debt debt = debtRepo.findById(debtId)
                .orElseThrow(() -> new RuntimeException("Debt not found"));
        BigDecimal totalPaid = paidRepo.getTotalPaidByDebt(debtId);
        BigDecimal totalReceived = receivedRepo.getTotalReceivedByDebt(debtId);
        System.out.println(debtId+" Total Paid: " + totalPaid + ", Total Received: " + totalReceived);
        return new DebtRecordSummaryResponseDto(
                debtId,
                totalPaid,
                totalReceived
        );
    }
}
