package com.expensetrace.app.service.debt;

import com.expensetrace.app.exception.ResourceNotFoundException;
import com.expensetrace.app.model.*;
import com.expensetrace.app.repository.DebtRepository;
import com.expensetrace.app.requestDto.DebtRequestDto;
import com.expensetrace.app.responseDto.DebtResponseDto;
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
public class DebtService implements IDebtService {

    private final DebtRepository debtRepo;
    private final SecurityUtil securityUtil;
    private final ModelMapper modelMapper;

    public DebtResponseDto createDebt(DebtRequestDto dto) {
        UUID userId = securityUtil.getAuthenticatedUserId();
        User user=new User();
        user.setId(userId);
        Debt debt = new Debt();
        debt.setUser(user);
        debt.setType(dto.getType());
        debt.setPersonName(dto.getPersonName());
        debt.setDueDate(dto.getDueDate());
        debt.setAdditionalDetail(dto.getAdditionalDetail());

        Debt savedTxn = debtRepo.save(debt);
        return modelMapper.map(savedTxn, DebtResponseDto.class);
    }

    public List<DebtResponseDto> getAllDebtsByUser() {
        UUID userId = securityUtil.getAuthenticatedUserId();
        return debtRepo.findByUserId(userId).stream()
                .map(debt -> modelMapper.map(debt, DebtResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<DebtResponseDto> getAllDebtsByUser(int page, int size) {
        UUID userId = securityUtil.getAuthenticatedUserId();
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending().and(Sort.by("time").descending()));

        Page<Debt> debtPage = debtRepo.findByUserId(userId, pageable);

        return debtPage.map(debt -> modelMapper.map(debt, DebtResponseDto.class));
    }


    public DebtResponseDto getDebtById(UUID id) {
        UUID userId = securityUtil.getAuthenticatedUserId();
        Debt debt = debtRepo.findById(id)
                .filter(t -> t.getUser().getId().equals(userId))
                .orElseThrow(() -> new ResourceNotFoundException("Debt not found"));
        return modelMapper.map(debt, DebtResponseDto.class);
    }

    public void deleteDebtById(UUID id) {
        UUID userId = securityUtil.getAuthenticatedUserId();
        Debt debt = debtRepo.findById(id)
                .filter(t -> t.getUser().getId().equals(userId))
                .orElseThrow(() -> new ResourceNotFoundException("Debt not found"));
        debtRepo.delete(debt);
    }

    public DebtResponseDto updateDebt(UUID id, DebtRequestDto dto) {
        UUID userId = securityUtil.getAuthenticatedUserId();
        Debt debt = debtRepo.findById(id)
                .filter(t -> t.getUser().getId().equals(userId))
                .orElseThrow(() -> new ResourceNotFoundException("Debt not found"));

        debt.setType(dto.getType());
        debt.setPersonName(dto.getPersonName());
        debt.setDueDate(dto.getDueDate());
        debt.setAdditionalDetail(dto.getAdditionalDetail());

        Debt updated = debtRepo.save(debt);
        return modelMapper.map(updated, DebtResponseDto.class);
    }
}
