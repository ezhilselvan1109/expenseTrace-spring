package com.expensetrace.app.service.tag;

import com.expensetrace.app.requestDto.TagRequestDto;
import com.expensetrace.app.model.Tag;
import com.expensetrace.app.responseDto.TagResponseDto;

import java.util.List;

public interface ITagService {
    TagResponseDto getTagById(Long id);
    List<TagResponseDto> getAllTagsByUser(Long userId);
    TagResponseDto addTag(TagRequestDto tagRequestDto,Long userId);
    TagResponseDto updateTag(TagRequestDto tagRequestDto, Long id);
    void deleteTagById(Long id);
}
