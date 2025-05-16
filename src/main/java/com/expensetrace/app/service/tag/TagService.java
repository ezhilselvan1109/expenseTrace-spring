package com.expensetrace.app.service.tag;

import com.expensetrace.app.model.Tag;
import com.expensetrace.app.model.Tag;
import com.expensetrace.app.model.User;
import com.expensetrace.app.requestDto.TagRequestDto;
import com.expensetrace.app.exception.AlreadyExistsException;
import com.expensetrace.app.exception.ResourceNotFoundException;
import com.expensetrace.app.model.Tag;
import com.expensetrace.app.repository.TagRepository;
import com.expensetrace.app.responseDto.PaymentModeResponseDto;
import com.expensetrace.app.responseDto.TagResponseDto;
import com.expensetrace.app.responseDto.TagResponseDto;
import com.expensetrace.app.responseDto.TagResponseDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService implements ITagService {
    private final TagRepository tagRepository;
    private final ModelMapper modelMapper;
    @Override
    public TagResponseDto getTagById(UUID id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found!"));
        return modelMapper.map(tag, TagResponseDto.class);
    }

    @Override
    public List<TagResponseDto> getAllTagsByUser(UUID userId) {
        return tagRepository.findByUserId(userId)
                .stream()
                .map(tag -> modelMapper.map(tag, TagResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public TagResponseDto addTag(TagRequestDto tagRequestDto,UUID userId) {
        if (tagRepository.existsByNameAndUserId(tagRequestDto.getName(),userId)) {
            throw new AlreadyExistsException(tagRequestDto.getName() + " already exists");
        }

        Tag tag = modelMapper.map(tagRequestDto, Tag.class);
        User user=new User();
        user.setId(userId);
        tag.setUser(user);

        Tag savedTag = tagRepository.save(tag);
        return modelMapper.map(savedTag, TagResponseDto.class);
    }

    @Override
    public TagResponseDto updateTag(TagRequestDto tagRequestDto, UUID id) {
        Tag existingTag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found!"));

        existingTag.setName(tagRequestDto.getName());

        Tag updatedTag = tagRepository.save(existingTag);
        return modelMapper.map(updatedTag, TagResponseDto.class);
    }


    @Override
    public void deleteTagById(UUID id) {
        tagRepository.findById(id)
                .ifPresentOrElse(tagRepository::delete, () -> {
                    throw new ResourceNotFoundException("Category not found!");
                });
    }
}
