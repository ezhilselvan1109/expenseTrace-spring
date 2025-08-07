package com.expensetrace.app.service.transaction.record.paid;

import com.expensetrace.app.dto.request.transaction.record.PaidRequestDto;
import com.expensetrace.app.dto.response.transaction.TransactionResponseDto;
import com.expensetrace.app.dto.response.transaction.record.AdjustmentResponseDto;
import com.expensetrace.app.dto.response.transaction.record.PaidResponseDto;
import com.expensetrace.app.enums.TransactionType;
import com.expensetrace.app.model.account.Account;
import com.expensetrace.app.model.account.PaymentMode;
import com.expensetrace.app.model.debt.Debt;
import com.expensetrace.app.model.transaction.Transaction;
import com.expensetrace.app.model.transaction.record.PaidRecord;
import com.expensetrace.app.repository.transaction.record.PaidRecordRepository;
import com.expensetrace.app.service.account.IAccountService;
import com.expensetrace.app.service.paymentMode.IPaymentModeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class PaidService implements IPaidService {

    private final PaidRecordRepository paidRecordRepo;

    private final IAccountService accountService;
    private final IPaymentModeService paymentModeService;

    private final ModelMapper modelMapper;

    public TransactionResponseDto save(Transaction transaction, PaidRequestDto dto) {
        PaidRecord txn = (PaidRecord) transaction;
        txn.setDebt(new Debt() {{
            setId(dto.getDebtId());
        }});
        setAccount(txn, dto.getAccountId());
        setPaymentMode(txn, dto.getPaymentModeId());
        Transaction saved = paidRecordRepo.save(txn);
        return modelMapper.map(saved, PaidResponseDto.class);
    }

    @Override
    public Page<TransactionResponseDto> getAllPaidRecords(UUID debtId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending().and(Sort.by("time").descending()));

        Page<Transaction> debtPage = paidRecordRepo.findByDebtIdAndType(debtId, TransactionType.PAID, pageable);

        return debtPage.map(debt -> modelMapper.map(debt, AdjustmentResponseDto.class));
    }

    @Override
    public BigDecimal getTotalPaid(UUID debtId) {
        return paidRecordRepo.findTotalAmountByDebtIdAndType(debtId, TransactionType.PAID);
    }

    public void setAccount(PaidRecord txn, UUID id) {
        if(id==null)return;
        Account account=accountService.getAccount(id);
        txn.setAccount(account);
    }

    public void setPaymentMode(PaidRecord txn, UUID id) {
        if(id==null)return;
        PaymentMode paymentMode = paymentModeService.getPaymentMode(id);
        txn.setPaymentMode(paymentMode);
    }
}
