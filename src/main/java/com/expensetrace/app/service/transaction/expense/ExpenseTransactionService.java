package com.expensetrace.app.service.transaction.expense;

import com.expensetrace.app.dto.request.transaction.ExpenseTransactionRequestDto;
import com.expensetrace.app.dto.response.transaction.ExpenseTransactionResponseDto;
import com.expensetrace.app.dto.response.transaction.TransactionResponseDto;
import com.expensetrace.app.model.Category;
import com.expensetrace.app.model.Tag;
import com.expensetrace.app.model.User;
import com.expensetrace.app.model.account.Account;
import com.expensetrace.app.model.account.PaymentMode;
import com.expensetrace.app.model.transaction.ExpenseTransaction;
import com.expensetrace.app.model.transaction.Transaction;
import com.expensetrace.app.repository.transaction.ExpenseTransactionRepository;
import com.expensetrace.app.service.account.IAccountService;
import com.expensetrace.app.service.category.ICategoryService;
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
public class ExpenseTransactionService implements IExpenseTransactionService {

    private final ExpenseTransactionRepository incomeTransactionRepo;

    private final IAccountService accountService;
    private final ICategoryService categoryService;
    private final ITagService tagService;
    private final IPaymentModeService paymentModeService;

    private final SecurityUtil securityUtil;
    private final ModelMapper modelMapper;

    public TransactionResponseDto save(ExpenseTransaction txn, ExpenseTransactionRequestDto dto) {
        System.out.println("Before ***********"+txn.getType()+" **** "+txn.getDescription()+" ******************************************************************************");
        txn.setUser(new User() {{
            setId(securityUtil.getAuthenticatedUserId());
        }});
        setCategory(txn, dto.getCategoryId());
        setAccount(txn, dto.getAccountId());
        setPaymentMode(txn, dto.getPaymentModeId());
        setTags(txn, dto);
        Transaction saved = incomeTransactionRepo.save(txn);
        System.out.println("***********"+saved.getType()+" **** "+saved.getDescription()+" ******************************************************************************");
        return modelMapper.map(saved, ExpenseTransactionResponseDto.class);
    }

    public Page<TransactionResponseDto> getAllExpenseTransactions(int page, int size) {
        UUID userId = securityUtil.getAuthenticatedUserId();
        Pageable pageable = PageRequest.of(page, size);
        return incomeTransactionRepo.findAllByUserId(userId, pageable)
                .map(txn -> modelMapper.map(txn, ExpenseTransactionResponseDto.class));
    }

    // --- Helpers ---

    public void setCategory(ExpenseTransaction txn, UUID id) {
        if(id==null)return;
        Category category=categoryService.getCategory(id);
        txn.setCategory(category);
    }

    public void setAccount(ExpenseTransaction txn, UUID id) {
        if(id==null)return;
        Account account=accountService.getAccount(id);
        txn.setAccount(account);
    }

    public void setPaymentMode(ExpenseTransaction txn, UUID id) {
        if(id==null)return;
        PaymentMode paymentMode = paymentModeService.getPaymentMode(id);
        txn.setPaymentMode(paymentMode);
    }

    public void setTags(ExpenseTransaction txn, ExpenseTransactionRequestDto dto) {
        Set<Tag> tag = tagService.resolveTags(dto.getTagIds(), dto.getTags());
        txn.setTags(tag);
    }
}