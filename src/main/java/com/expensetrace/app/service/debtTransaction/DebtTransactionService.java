package com.expensetrace.app.service.debtTransaction;


import com.expensetrace.app.exception.ResourceNotFoundException;
import com.expensetrace.app.model.Account;
import com.expensetrace.app.model.Debt;
import com.expensetrace.app.model.DebtTransaction;
import com.expensetrace.app.repository.DebtTransactionRepository;
import com.expensetrace.app.requestDto.DebtTransactionRequestDto;
import com.expensetrace.app.responseDto.DebtTransactionResponseDto;
import com.expensetrace.app.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DebtTransactionService implements IDebtTransactionService {

    private final DebtTransactionRepository debtRepo;
    private final SecurityUtil securityUtil;
    private final ModelMapper modelMapper;

    public DebtTransactionResponseDto createDebtTransaction(UUID debtId, DebtTransactionRequestDto dto) {
        Debt debt=new Debt();
        debt.setId(debtId);
        Account account=new Account() {
            @Override
            public void setId(UUID id) {
                super.setId(id);
            }
        };
        account.setId(dto.getAccountId());

        DebtTransaction debtTransaction = new DebtTransaction();
        debtTransaction.setDebt(debt);
        debtTransaction.setType(dto.getType());
        debtTransaction.setDate(dto.getDate());
        debtTransaction.setTime(dto.getTime());
        debtTransaction.setAccount(account);
        debtTransaction.setDescription(dto.getDescription());

        DebtTransaction savedTxn = debtRepo.save(debtTransaction);
        return modelMapper.map(savedTxn, DebtTransactionResponseDto.class);
    }

    public List<DebtTransactionResponseDto> getAllDebtTransactionsByUser(UUID debtId) {
        return debtRepo.findByDebtId(debtId).stream()
                .map(debt -> modelMapper.map(debt, DebtTransactionResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<DebtTransactionResponseDto> getAllDebtTransactionsByUser(UUID debtId,int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending().and(Sort.by("time").descending()));

        Page<DebtTransaction> debtPage = debtRepo.findByDebtId(debtId, pageable);

        return debtPage.map(debt -> modelMapper.map(debt, DebtTransactionResponseDto.class));
    }


    public DebtTransactionResponseDto getDebtTransactionById(UUID id) {
        DebtTransaction debt = debtRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DebtTransaction not found"));
        return modelMapper.map(debt, DebtTransactionResponseDto.class);
    }

    public void deleteDebtTransactionById(UUID id) {
        DebtTransaction debt = debtRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DebtTransaction not found"));
        debtRepo.delete(debt);
    }

    public DebtTransactionResponseDto updateDebtTransaction(UUID id, DebtTransactionRequestDto dto) {
        DebtTransaction debtTransaction = debtRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DebtTransaction not found"));

        Account account=new Account() {
            @Override
            public void setId(UUID id) {
                super.setId(id);
            }
        };
        Debt debt=new Debt();
        debt.setId(id);
        debtTransaction.setDebt(debt);
        debtTransaction.setType(dto.getType());
        debtTransaction.setDate(dto.getDate());
        debtTransaction.setTime(dto.getTime());
        debtTransaction.setAccount(account);
        debtTransaction.setDescription(dto.getDescription());

        DebtTransaction updated = debtRepo.save(debtTransaction);
        return modelMapper.map(updated, DebtTransactionResponseDto.class);
    }
}
