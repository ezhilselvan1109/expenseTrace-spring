package com.expensetrace.app.service.debt;

import com.expensetrace.app.enums.DebtType;
import com.expensetrace.app.exception.ResourceNotFoundException;
import com.expensetrace.app.model.User;
import com.expensetrace.app.model.debt.Debt;
import com.expensetrace.app.model.transaction.Transaction;
import com.expensetrace.app.model.transaction.record.AdjustmentRecord;
import com.expensetrace.app.model.transaction.record.PaidRecord;
import com.expensetrace.app.model.transaction.record.ReceivedRecord;
import com.expensetrace.app.repository.DebtRepository;
import com.expensetrace.app.dto.request.DebtRequestDto;
import com.expensetrace.app.dto.response.debt.DebtResponseDto;
import com.expensetrace.app.dto.response.debt.DebtSummaryResponseDto;
import com.expensetrace.app.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DebtService implements IDebtService {

    private final DebtRepository debtRepo;
    private final SecurityUtil securityUtil;
    private final ModelMapper modelMapper;

    public DebtResponseDto createDebt(DebtRequestDto dto) {
        UUID userId = securityUtil.getAuthenticatedUserId();
        User user = new User();
        user.setId(userId);

        Debt debt = new Debt();
        debt.setUser(user);
        debt.setType(dto.getType());
        debt.setPersonName(dto.getPersonName());
        debt.setDueDate(dto.getDueDate());
        debt.setAdditionalDetail(dto.getAdditionalDetail());

        Debt savedDebt = debtRepo.save(debt);
        return mapToResponseDto(savedDebt);
    }

    public List<DebtResponseDto> getAllDebtsByUser() {
        UUID userId = securityUtil.getAuthenticatedUserId();
        return debtRepo.findByUserId(userId).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<DebtResponseDto> getAllDebtsByUser(int page, int size) {
        UUID userId = securityUtil.getAuthenticatedUserId();
        Pageable pageable = PageRequest.of(page, size);
        Page<Debt> debtPage = debtRepo.findByUserId(userId, pageable);

        return debtPage.map(this::mapToResponseDto);
    }

    @Override
    public Page<DebtResponseDto> getAllBorrowingDebtsByUser(int page, int size) {
        UUID userId = securityUtil.getAuthenticatedUserId();
        Pageable pageable = PageRequest.of(page, size);
        Page<Debt> debtPage = debtRepo.findByUserIdAndType(userId, DebtType.BORROWING, pageable);

        return debtPage.map(this::mapToResponseDto);
    }

    @Override
    public Page<DebtResponseDto> getAllLendingDebtsByUser(int page, int size) {
        UUID userId = securityUtil.getAuthenticatedUserId();
        Pageable pageable = PageRequest.of(page, size);
        Page<Debt> debtPage = debtRepo.findByUserIdAndType(userId, DebtType.LENDING, pageable);

        return debtPage.map(this::mapToResponseDto);
    }

    public DebtResponseDto getDebtById(UUID id) {
        UUID userId = securityUtil.getAuthenticatedUserId();
        Debt debt = debtRepo.findById(id)
                .filter(d -> d.getUser().getId().equals(userId))
                .orElseThrow(() -> new ResourceNotFoundException("Debt not found"));

        return mapToResponseDto(debt);
    }

    public void deleteDebtById(UUID id) {
        UUID userId = securityUtil.getAuthenticatedUserId();
        Debt debt = debtRepo.findById(id)
                .filter(d -> d.getUser().getId().equals(userId))
                .orElseThrow(() -> new ResourceNotFoundException("Debt not found"));

        debtRepo.delete(debt);
    }

    public DebtResponseDto updateDebt(UUID id, DebtRequestDto dto) {
        UUID userId = securityUtil.getAuthenticatedUserId();
        Debt debt = debtRepo.findById(id)
                .filter(d -> d.getUser().getId().equals(userId))
                .orElseThrow(() -> new ResourceNotFoundException("Debt not found"));

        debt.setType(dto.getType());
        debt.setPersonName(dto.getPersonName());
        debt.setDueDate(dto.getDueDate());
        debt.setAdditionalDetail(dto.getAdditionalDetail());

        Debt updatedDebt = debtRepo.save(debt);
        return mapToResponseDto(updatedDebt);
    }

    @Override
    public DebtSummaryResponseDto getPayableAndReceivableSummary() {
        UUID userId = securityUtil.getAuthenticatedUserId();

        BigDecimal totalPayable = debtRepo.sumAmountByUserAndType(userId, DebtType.BORROWING);
        BigDecimal totalReceivable = debtRepo.sumAmountByUserAndType(userId, DebtType.LENDING);

        return new DebtSummaryResponseDto(totalPayable, totalReceivable);
    }



    private DebtResponseDto mapToResponseDto(Debt debt) {
        DebtResponseDto dto = modelMapper.map(debt, DebtResponseDto.class);

        BigDecimal totalReceived = debt.getRecords().stream()
                .filter(r -> r instanceof ReceivedRecord)
                .map(r -> r.getAmount() == null ? BigDecimal.ZERO : r.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalPaid = debt.getRecords().stream()
                .filter(r -> r instanceof PaidRecord)
                .map(r -> r.getAmount() == null ? BigDecimal.ZERO : r.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalAdjustment = debt.getRecords().stream()
                .filter(r -> r instanceof AdjustmentRecord)
                .map(r -> r.getAmount() == null ? BigDecimal.ZERO : r.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal balance;
        if (debt.getType() == DebtType.LENDING) {
            balance = totalReceived.subtract(totalPaid).add(totalAdjustment);
        } else {
            balance = totalPaid.subtract(totalReceived).add(totalAdjustment);
        }

        dto.setAmount(balance);
       return dto;
    }
}
