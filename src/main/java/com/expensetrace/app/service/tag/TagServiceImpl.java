package com.expensetrace.app.service.tag;

import com.expensetrace.app.dto.response.tag.TagResponseDto;
import com.expensetrace.app.dto.response.tag.TagsResponseDto;
import com.expensetrace.app.model.Tag;
import com.expensetrace.app.model.User;
import com.expensetrace.app.model.schedule.ScheduledTransaction;
import com.expensetrace.app.model.transaction.TaggableTransaction;
import com.expensetrace.app.repository.TagRepository;
import com.expensetrace.app.repository.transaction.TransactionRepository;
import com.expensetrace.app.service.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;

    @Override
    @Transactional(readOnly = true)
    public Page<TagsResponseDto> getAllTags(Pageable pageable) {
        UUID userId = userService.getAuthenticatedUser().getId();
        return tagRepository.findAllByUserId(userId, pageable)
                .map(tag -> {
                    TagsResponseDto dto = new TagsResponseDto();
                    dto.setTag(modelMapper.map(tag, TagResponseDto.class));

                    // Set transaction counts
                    dto.setTransactions(tag.getTransactionsCount());
                    dto.setScheduledTransactions(tag.getScheduledTransactionsCount());

                    return dto;
                });
    }

    @Override
    @Transactional
    public void deleteTag(UUID tagId) {
        UUID userId = userService.getAuthenticatedUser().getId();
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found"));

        if (!tag.getUser().getId().equals(userId)) {
            throw new SecurityException("Unauthorized");
        }

        // Remove relationships from TaggableTransaction
        for (TaggableTransaction txn : tag.getTransactions()) {
            txn.getTags().remove(tag);
        }

        // Remove relationships from ScheduledTransaction
        for (ScheduledTransaction schedTxn : tag.getScheduledTransactions()) {
            schedTxn.getTags().remove(tag);
        }

        tagRepository.delete(tag);
    }

    @Override
    @Transactional
    public void mergeTags(UUID sourceTagId, UUID targetTagId) {
        UUID userId = userService.getAuthenticatedUser().getId();

        if (sourceTagId.equals(targetTagId)) {
            throw new IllegalArgumentException("Source and target tags cannot be the same");
        }

        Tag source = tagRepository.findById(sourceTagId)
                .orElseThrow(() -> new EntityNotFoundException("Source tag not found"));
        Tag target = tagRepository.findById(targetTagId)
                .orElseThrow(() -> new EntityNotFoundException("Target tag not found"));

        if (!source.getUser().getId().equals(userId) || !target.getUser().getId().equals(userId)) {
            throw new SecurityException("Unauthorized");
        }

        // Reassign all TaggableTransaction associations
        for (TaggableTransaction txn : new HashSet<>(source.getTransactions())) {
            txn.getTags().remove(source);
            txn.getTags().add(target);
        }

        // Reassign all ScheduledTransaction associations
        for (ScheduledTransaction schedTxn : new HashSet<>(source.getScheduledTransactions())) {
            schedTxn.getTags().remove(source);
            schedTxn.getTags().add(target);
        }

        // Clear source tag collections to avoid Hibernate FK issues
        source.getTransactions().clear();
        source.getScheduledTransactions().clear();

        // Delete the source tag
        tagRepository.delete(source);
    }


    @Override
    @Transactional
    public TagsResponseDto updateTag(UUID tagId, String name) {
        UUID userId = userService.getAuthenticatedUser().getId();

        // Find the tag
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found"));

        // Check ownership
        if (!tag.getUser().getId().equals(userId)) {
            throw new SecurityException("Unauthorized");
        }

        // Check for duplicate name for this user
        boolean exists = tagRepository.existsByUserIdAndName(userId, name);
        if (exists && !tag.getName().equals(name)) {
            throw new IllegalArgumentException("Tag name already exists for this user");
        }

        // Update the name
        tag.setName(name);
        Tag updatedTag = tagRepository.save(tag);

        // Map to DTO including counts
        TagsResponseDto dto = new TagsResponseDto();
        TagResponseDto tagDto = new TagResponseDto();
        tagDto.setId(updatedTag.getId());
        tagDto.setName(updatedTag.getName());
        dto.setTag(tagDto);

        dto.setTransactions(updatedTag.getTransactionsCount());
        dto.setScheduledTransactions(updatedTag.getScheduledTransactionsCount());

        return dto;
    }
}