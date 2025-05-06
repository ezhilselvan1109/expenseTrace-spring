package com.expensetrace.app.service.tag;

import com.expensetrace.app.requestDto.TagRequestDto;
import com.expensetrace.app.exception.AlreadyExistsException;
import com.expensetrace.app.exception.ResourceNotFoundException;
import com.expensetrace.app.model.Tag;
import com.expensetrace.app.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagService implements ITagService {
    private final TagRepository tagRepository;
    private final ModelMapper modelMapper;
    @Override
    public Tag getTagById(Long id) {
        return tagRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Tag not found!"));
    }

    @Override
    public Tag getTagByName(String name) {
        return tagRepository.findByName(name);
    }

    @Override
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    @Override
    public Tag addTag(TagRequestDto tagRequestDto) {
        Tag tag = modelMapper.map(tagRequestDto, Tag.class);
        return  Optional.of(tag)
                .map(tagRepository :: save)
                .orElseThrow(() -> new AlreadyExistsException(tag.getName()+" already exists"));
    }

    @Override
    public Tag updateTag(TagRequestDto tagRequestDto, Long id) {
        return Optional.ofNullable(getTagById(id)).map(oldTag -> {
            oldTag.setName(tagRequestDto.getName());
            return tagRepository.save(oldTag);
        }) .orElseThrow(()-> new ResourceNotFoundException("Tag not found!"));
    }


    @Override
    public void deleteTagById(Long id) {
        tagRepository.findById(id)
                .ifPresentOrElse(tagRepository::delete, () -> {
                    throw new ResourceNotFoundException("Category not found!");
                });
    }
}
