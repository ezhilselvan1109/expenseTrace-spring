package com.expensetrace.app.service.transaction.impl.debt;

import com.expensetrace.app.dto.request.transaction.record.DebtPaidRequestDto;
import com.expensetrace.app.dto.response.transaction.record.DebtPaidResponseDto;
import com.expensetrace.app.account.model.Account;
import com.expensetrace.app.account.model.PaymentMode;
import com.expensetrace.app.model.debt.Debt;
import com.expensetrace.app.model.transaction.record.PaidRecord;
import com.expensetrace.app.repository.DebtRepository;
import com.expensetrace.app.account.repository.AccountRepository;
import com.expensetrace.app.account.repository.PaymentModeRepository;
import com.expensetrace.app.repository.transaction.debt.PaidRecordRepository;
import com.expensetrace.app.service.transaction.DebtTransactionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service("debtPaidService")
public class PaidRecordServiceImpl implements DebtTransactionService<DebtPaidRequestDto, DebtPaidResponseDto> {
    private final PaidRecordRepository repository;
    private final ModelMapper mapper;
    private final AccountRepository accountRepository;
    private final PaymentModeRepository paymentModeRepository;
    private final DebtRepository debtRepository;

    @Override
    public DebtPaidResponseDto create(DebtPaidRequestDto request, UUID debtId) {
        PaidRecord transaction = new PaidRecord();

        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (request.getPaymentModeId() != null) {
            PaymentMode paymentMode = paymentModeRepository.findById(request.getPaymentModeId())
                    .orElseThrow(() -> new RuntimeException("Payment mode not found"));
            transaction.setPaymentMode(paymentMode);
        }

        Debt debt = debtRepository.findById(debtId)
                .orElseThrow(() -> new RuntimeException("Debt not found"));
        transaction.setDebt(debt);
        transaction.setTxnDate(request.getTxnDate());
        transaction.setTxnTime(request.getTxnTime());
        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setAccount(account);

        PaidRecord saved = repository.save(transaction);

        return mapper.map(saved, DebtPaidResponseDto.class);
    }

    @Override
    public DebtPaidResponseDto update(UUID id, DebtPaidRequestDto request) {
        PaidRecord transaction = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setTxnDate(request.getTxnDate());
        transaction.setTxnTime(request.getTxnTime());

        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));
        transaction.setAccount(account);

        if (request.getPaymentModeId() != null) {
            PaymentMode paymentMode = paymentModeRepository.findById(request.getPaymentModeId())
                    .orElseThrow(() -> new RuntimeException("Payment mode not found"));
            transaction.setPaymentMode(paymentMode);
        } else {
            transaction.setPaymentMode(null);
        }

        PaidRecord saved = repository.save(transaction);
        return mapper.map(saved, DebtPaidResponseDto.class);
    }
}
