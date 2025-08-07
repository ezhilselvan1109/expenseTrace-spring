package com.expensetrace.app.service.transaction.adjustment;

import com.expensetrace.app.dto.request.transaction.AdjustmentTransactionRequestDto;
import com.expensetrace.app.dto.response.transaction.AdjustmentTransactionResponseDto;
import com.expensetrace.app.dto.response.transaction.TransactionResponseDto;
import com.expensetrace.app.model.User;
import com.expensetrace.app.model.account.Account;
import com.expensetrace.app.model.transaction.AdjustmentTransaction;
import com.expensetrace.app.model.transaction.Transaction;
import com.expensetrace.app.repository.transaction.AdjustmentTransactionRepository;
import com.expensetrace.app.service.account.IAccountService;
import com.expensetrace.app.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdjustmentTransactionService implements IAdjustmentTransactionService {

    private final AdjustmentTransactionRepository incomeTransactionRepo;

    private final IAccountService accountService;

    private final SecurityUtil securityUtil;
    private final ModelMapper modelMapper;

    public TransactionResponseDto save(AdjustmentTransaction txn, AdjustmentTransactionRequestDto dto) {
        txn.setUser(new User() {{
            setId(securityUtil.getAuthenticatedUserId());
        }});
        setAccount(txn, dto.getAccountId());
        Transaction saved = incomeTransactionRepo.save(txn);
        return modelMapper.map(saved, AdjustmentTransactionResponseDto.class);
    }

    public Page<TransactionResponseDto> getAllAdjustmentTransactions(int page, int size) {
        UUID userId = securityUtil.getAuthenticatedUserId();
        Pageable pageable = PageRequest.of(page, size);
        return incomeTransactionRepo.findAllByUserId(userId, pageable)
                .map(txn -> modelMapper.map(txn, AdjustmentTransactionResponseDto.class));
    }

    // --- Helpers ---

    public void setAccount(AdjustmentTransaction txn, UUID id) {
        if(id==null)return;
        Account account=accountService.getAccount(id);
        txn.setAccount(account);
    }
}