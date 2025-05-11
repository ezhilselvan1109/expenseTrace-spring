package com.expensetrace.app.service.transaction;

import com.expensetrace.app.exception.ResourceNotFoundException;
import com.expensetrace.app.model.*;
import com.expensetrace.app.repository.*;
import com.expensetrace.app.requestDto.TransactionRequestDTO;
import com.expensetrace.app.responseDto.TransactionResponseDTO;
import com.expensetrace.app.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService implements  ITransactionService{

    private final TransactionRepository transactionRepo;
    private final CategoryRepository categoryRepo;
    private final AccountRepository accountRepo;
    private final TagRepository tagRepo;
    private final SecurityUtil securityUtil;
    private final ModelMapper modelMapper;

    public TransactionResponseDTO createTransaction(TransactionRequestDTO dto) {
        Long userId = securityUtil.getAuthenticatedUserId();
        User user=new User();
        user.setId(userId);
        Transaction txn = new Transaction();
        txn.setUser(user);
        txn.setType(dto.getType());
        txn.setDate(dto.getDate());
        txn.setTime(dto.getTime());
        txn.setAmount(dto.getAmount());
        txn.setDescription(dto.getDescription());

        // Set category
        if (dto.getCategoryId() != null) {
            Category category = categoryRepo.findById(dto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            txn.setCategory(category);
        }

        // Set account
        if (dto.getAccountId() != null) {
            Account account = accountRepo.findById(dto.getAccountId())
                    .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
            txn.setAccount(account);
        }

        // Set tags
        if (dto.getTagIds() != null && !dto.getTagIds().isEmpty()) {
            List<Tag> tags = tagRepo.findAllById(dto.getTagIds());
            txn.setTags(new HashSet<>(tags));
        }

        Transaction savedTxn = transactionRepo.save(txn);
        return modelMapper.map(savedTxn, TransactionResponseDTO.class);
    }

    public List<TransactionResponseDTO> getAllTransactionsByUser() {
        Long userId = securityUtil.getAuthenticatedUserId();
        return transactionRepo.findByUserId(userId).stream()
                .map(txn -> modelMapper.map(txn, TransactionResponseDTO.class))
                .collect(Collectors.toList());
    }

    public TransactionResponseDTO getTransactionById(Long id) {
        Long userId = securityUtil.getAuthenticatedUserId();
        Transaction txn = transactionRepo.findById(id)
                .filter(t -> t.getUser().getId().equals(userId))
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
        return modelMapper.map(txn, TransactionResponseDTO.class);
    }

    public void deleteTransactionById(Long id) {
        Long userId = securityUtil.getAuthenticatedUserId();
        Transaction txn = transactionRepo.findById(id)
                .filter(t -> t.getUser().getId().equals(userId))
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
        transactionRepo.delete(txn);
    }

    public TransactionResponseDTO updateTransaction(Long id, TransactionRequestDTO dto) {
        Long userId = securityUtil.getAuthenticatedUserId();
        Transaction txn = transactionRepo.findById(id)
                .filter(t -> t.getUser().getId().equals(userId))
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        txn.setType(dto.getType());
        txn.setDate(dto.getDate());
        txn.setTime(dto.getTime());
        txn.setAmount(dto.getAmount());
        txn.setDescription(dto.getDescription());

        if (dto.getCategoryId() != null) {
            Category category = categoryRepo.findById(dto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            txn.setCategory(category);
        }

        if (dto.getAccountId() != null) {
            Account account = accountRepo.findById(dto.getAccountId())
                    .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
            txn.setAccount(account);
        }

        if (dto.getTagIds() != null) {
            List<Tag> tags = tagRepo.findAllById(dto.getTagIds());
            txn.setTags(new HashSet<>(tags));
        }

        Transaction updated = transactionRepo.save(txn);
        return modelMapper.map(updated, TransactionResponseDTO.class);
    }
}
