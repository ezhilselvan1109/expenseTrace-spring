package com.expensetrace.app.service.debt;

import com.expensetrace.app.dto.request.DebtRequestDto;
import com.expensetrace.app.dto.response.debt.DebtResponseDto;
import com.expensetrace.app.dto.response.debt.DebtSummaryResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface IDebtService {
    DebtResponseDto createDebt(DebtRequestDto dto);
    List<DebtResponseDto> getAllDebtsByUser();
    Page<DebtResponseDto> getAllDebtsByUser(int page, int size);
    Page<DebtResponseDto> getAllBorrowingDebtsByUser(int page, int size);
    Page<DebtResponseDto> getAllLendingDebtsByUser(int page, int size);
    DebtResponseDto getDebtById(UUID id);
    void deleteDebtById(UUID id);
    DebtResponseDto updateDebt(UUID id, DebtRequestDto dto);
    DebtSummaryResponseDto getPayableAndReceivableSummary();

}
