package com.expensetrace.app.service.budget.strategy;

import com.expensetrace.app.dto.request.budget.BudgetRequestDto;
import com.expensetrace.app.dto.request.budget.YearlyBudgetRequestDto;
import com.expensetrace.app.dto.response.budget.BudgetListResponseDto;
import com.expensetrace.app.dto.response.budget.BudgetResponseDto;
import com.expensetrace.app.model.User;
import com.expensetrace.app.model.budget.YearlyBudget;
import com.expensetrace.app.repository.UserRepository;
import com.expensetrace.app.repository.YearlyBudgetRepository;
import com.expensetrace.app.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class YearlyBudgetStrategy implements BudgetStrategy {

    private final YearlyBudgetRepository repository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final SecurityUtil securityUtil;

    @Override
    public BudgetResponseDto create(BudgetRequestDto dto) {
        UUID userId = securityUtil.getAuthenticatedUserId();
        YearlyBudgetRequestDto yearlyDto = (YearlyBudgetRequestDto) dto;
        YearlyBudget budget = mapper.map(yearlyDto, YearlyBudget.class);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        budget.setUser(user);

        budget.setId(UUID.randomUUID());
        YearlyBudget saved = repository.save(budget);
        return mapper.map(saved, BudgetResponseDto.class);
    }

    @Override
    public BudgetResponseDto get(UUID budgetId) {
        YearlyBudget budget = repository.findById(budgetId)
                .orElseThrow(() -> new RuntimeException("Yearly budget not found"));
        return mapper.map(budget, BudgetResponseDto.class);
    }

    @Override
    public BudgetListResponseDto getAll() {
        List<YearlyBudget> budgets = repository.findAll();
        int currentYear = LocalDate.now().getYear();

        List<BudgetResponseDto> past = new ArrayList<>();
        List<BudgetResponseDto> present = new ArrayList<>();
        List<BudgetResponseDto> upcoming = new ArrayList<>();

        for (YearlyBudget b : budgets) {
            BudgetResponseDto dto = mapper.map(b, BudgetResponseDto.class);

            if (b.getYear() < currentYear) {
                past.add(dto);
            } else if (b.getYear() == currentYear) {
                present.add(dto);
            } else {
                upcoming.add(dto);
            }
        }

        BudgetListResponseDto response = new BudgetListResponseDto();
        response.setPast(past);
        response.setPresent(present);
        response.setUpcoming(upcoming);
        return response;
    }

    @Override
    public BudgetResponseDto update(UUID id, BudgetRequestDto dto) {
        YearlyBudget budget = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Yearly budget not found"));

        YearlyBudgetRequestDto yearlyDto = (YearlyBudgetRequestDto) dto;
        budget.setYear(yearlyDto.getYear());
        budget.setTotalLimit(yearlyDto.getTotalLimit());

        YearlyBudget updated = repository.save(budget);
        return mapper.map(updated, BudgetResponseDto.class);
    }

    @Override
    public void delete(UUID budgetId) {
        repository.deleteById(budgetId);
    }
}