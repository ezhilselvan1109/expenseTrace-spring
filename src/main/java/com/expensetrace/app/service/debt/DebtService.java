package com.expensetrace.app.service.debt;

import com.expensetrace.app.enums.DebtType;
import com.expensetrace.app.exception.ResourceNotFoundException;
import com.expensetrace.app.model.User;
import com.expensetrace.app.model.debt.Debt;
import com.expensetrace.app.model.transaction.Transaction;
import com.expensetrace.app.model.transaction.record.PaidRecord;
import com.expensetrace.app.model.transaction.record.ReceivedRecord;
import com.expensetrace.app.repository.DebtRepository;
import com.expensetrace.app.dto.request.DebtRequestDto;
import com.expensetrace.app.dto.response.DebtResponseDto;
import com.expensetrace.app.dto.response.DebtSummaryResponseDto;
import com.expensetrace.app.service.transaction.ITransactionService;
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
    private final ITransactionService transactionService;

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
        transactionService.createTransaction(savedDebt.getId(), dto.getRecord());

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

    private DebtResponseDto mapToResponseDto(Debt debt) {
        DebtResponseDto dto = modelMapper.map(debt, DebtResponseDto.class);
        dto.setAmount(calculateNetAmount(debt.getPaidRecords(), debt.getReceivedRecords()));
        return dto;
    }

    private BigDecimal calculateNetAmount(List<? extends Transaction> paidRecords,
                                          List<? extends Transaction> receivedRecords) {
        BigDecimal totalPaid = (paidRecords == null) ? BigDecimal.ZERO :
                paidRecords.stream()
                        .map(Transaction::getAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalReceived = (receivedRecords == null) ? BigDecimal.ZERO :
                receivedRecords.stream()
                        .map(Transaction::getAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalPaid.subtract(totalReceived).abs();
    }

    public BigDecimal getTotalPayableAmount() {
        UUID userId = securityUtil.getAuthenticatedUserId();
        List<Debt> borrowingDebts = debtRepo.findByUserIdAndType(userId, DebtType.BORROWING, Pageable.unpaged()).getContent();

        return borrowingDebts.stream()
                .map(debt -> calculateNetAmount(debt.getPaidRecords(), debt.getReceivedRecords()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalReceivableAmount() {
        UUID userId = securityUtil.getAuthenticatedUserId();
        List<Debt> lendingDebts = debtRepo.findByUserIdAndType(userId, DebtType.LENDING, Pageable.unpaged()).getContent();

        return lendingDebts.stream()
                .map(debt -> calculateNetAmount(debt.getPaidRecords(), debt.getReceivedRecords()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public DebtSummaryResponseDto getPayableAndReceivableSummary() {
        UUID userId = securityUtil.getAuthenticatedUserId();

        List<Debt> borrowingDebts = debtRepo.findByUserIdAndType(userId, DebtType.BORROWING, Pageable.unpaged()).getContent();
        List<Debt> lendingDebts = debtRepo.findByUserIdAndType(userId, DebtType.LENDING, Pageable.unpaged()).getContent();

        BigDecimal totalPayable = borrowingDebts.stream()
                .map(debt -> calculateNetAmount(debt.getPaidRecords(), debt.getReceivedRecords()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalReceivable = lendingDebts.stream()
                .map(debt -> calculateNetAmount(debt.getPaidRecords(), debt.getReceivedRecords()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new DebtSummaryResponseDto(totalPayable, totalReceivable);
    }
}
