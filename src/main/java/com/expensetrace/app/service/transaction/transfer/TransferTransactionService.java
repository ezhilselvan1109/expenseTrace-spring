package com.expensetrace.app.service.transaction.transfer;

import com.expensetrace.app.dto.request.transaction.TransferTransactionRequestDto;
import com.expensetrace.app.dto.response.transaction.TransferTransactionResponseDto;
import com.expensetrace.app.dto.response.transaction.TransactionResponseDto;
import com.expensetrace.app.model.Tag;
import com.expensetrace.app.model.User;
import com.expensetrace.app.model.account.Account;
import com.expensetrace.app.model.account.PaymentMode;
import com.expensetrace.app.model.transaction.TransferTransaction;
import com.expensetrace.app.model.transaction.Transaction;
import com.expensetrace.app.repository.transaction.TransferTransactionRepository;
import com.expensetrace.app.service.account.IAccountService;
import com.expensetrace.app.service.paymentMode.IPaymentModeService;
import com.expensetrace.app.service.tag.ITagService;
import com.expensetrace.app.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransferTransactionService implements ITransferTransactionService {

    private final TransferTransactionRepository transferTransactionRepo;

    private final IAccountService accountService;
    private final ITagService tagService;
    private final IPaymentModeService paymentModeService;

    private final SecurityUtil securityUtil;
    private final ModelMapper modelMapper;

    public TransactionResponseDto save(TransferTransaction txn, TransferTransactionRequestDto dto) {
        txn.setUser(new User() {{
            setId(securityUtil.getAuthenticatedUserId());
        }});
        setFromAccount(txn, dto.getFromAccountId());
        setFromPaymentMode(txn, dto.getFromPaymentModeId());
        setTags(txn, dto);
        txn.setUser(new User() {{
            setId(securityUtil.getAuthenticatedUserId());
        }});
        setFromAccount(txn, dto.getFromAccountId());
        setToAccount(txn, dto.getToAccountId());

        setFromPaymentMode(txn, dto.getFromAccountId());
        setToPaymentMode(txn, dto.getToPaymentModeId());

        setTags(txn,dto);
        Transaction saved = transferTransactionRepo.save(txn);
        return modelMapper.map(saved, TransferTransactionResponseDto.class);
    }


    public Page<TransactionResponseDto> getAllTransferTransactions(int page, int size) {
        UUID userId = securityUtil.getAuthenticatedUserId();
        Pageable pageable = PageRequest.of(page, size);
        return transferTransactionRepo.findAllByUserId(userId, pageable)
                .map(txn -> modelMapper.map(txn, TransferTransactionResponseDto.class));
    }

    // --- Helpers ---

    public void setFromAccount(TransferTransaction txn, UUID id) {
        if(id==null)return;
        Account account=accountService.getAccount(id);
        txn.setFromAccount(account);
    }

    public void setFromPaymentMode(TransferTransaction txn, UUID id) {
        if(id==null)return;
        PaymentMode paymentMode = paymentModeService.getPaymentMode(id);
        txn.setFromPaymentMode(paymentMode);
    }

    public void setToAccount(TransferTransaction txn, UUID id) {
        if(id==null)return;
        Account account=accountService.getAccount(id);
        txn.setToAccount(account);
    }

    public void setToPaymentMode(TransferTransaction txn, UUID id) {
        if(id==null)return;
        PaymentMode paymentMode = paymentModeService.getPaymentMode(id);
        txn.setToPaymentMode(paymentMode);
    }

    public void setTags(TransferTransaction txn, TransferTransactionRequestDto dto) {
        Set<Tag> tag = tagService.resolveTags(dto.getTagIds(), dto.getTags());
        txn.setTags(tag);
    }
}