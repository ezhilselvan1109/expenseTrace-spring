package com.expensetrace.app.service;

import com.expensetrace.app.requestDto.TransactionRequestDTO;
import com.expensetrace.app.model.*;
import com.expensetrace.app.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

@Service
public class TransactionService {

    private TransactionRepository transactionRepo;
    private UserRepository userRepo;
    private CategoryRepository categoryRepo;
    private AccountRepository accountRepo;
    private TagRepository tagRepo;
    private AttachmentRepository attachmentRepo;

    public Transaction createTransaction(TransactionRequestDTO dto) throws IOException {
        Transaction txn = new Transaction();
        txn.setType(dto.getType());
        txn.setDate(dto.getDate());
        txn.setTime(dto.getTime());
        txn.setAmount(dto.getAmount());
        txn.setDescription(dto.getDescription());

        User user = userRepo.findById(dto.getUserId()).orElseThrow();
        txn.setUser(user);

        Category category = categoryRepo.findById(dto.getCategoryId()).orElseThrow();
        txn.setCategory(category);

        Account account = accountRepo.findById(dto.getAccountId()).orElseThrow();
        txn.setAccount(account);

        // Set tags
        if (dto.getTagIds() != null && !dto.getTagIds().isEmpty()) {
            List<Tag> tags = tagRepo.findAllById(dto.getTagIds());
            txn.setTags(new HashSet<>(tags));
        }

        Transaction savedTxn = transactionRepo.save(txn);

        // Handle attachment
        if (dto.getAttachment() != null && !dto.getAttachment().isEmpty()) {
            MultipartFile file = dto.getAttachment();

            Attachment attachment = new Attachment();
            attachment.setTransaction(savedTxn);
            attachment.setFileName(file.getOriginalFilename());
            attachment.setContentType(file.getContentType());
            attachment.setData(file.getBytes());

            attachmentRepo.save(attachment);
        }

        return savedTxn;
    }

    public List<Transaction> getAllTransactionsByUser(Long userId) {
        return transactionRepo.findByUserId(userId);
    }

    public Transaction getTransactionById(Long id) {
        return transactionRepo.findById(id).orElseThrow();
    }

    public void uploadAttachment(Long transactionId, MultipartFile file) throws IOException {
        Transaction txn = transactionRepo.findById(transactionId).orElseThrow();

        Attachment attachment = new Attachment();
        attachment.setTransaction(txn);
        attachment.setFileName(file.getOriginalFilename());
        attachment.setContentType(file.getContentType());
        attachment.setData(file.getBytes());

        attachmentRepo.save(attachment);
    }

}

