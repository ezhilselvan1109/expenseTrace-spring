package com.expensetrace.app.service.tag;

import com.expensetrace.app.requestDto.TagRequestDto;
import com.expensetrace.app.model.Tag;

import java.util.List;

public interface ITagService {
    Tag getTagById(Long id);
    Tag getTagByName(String name);
    List<Tag> getAllTags();
    Tag addTag(TagRequestDto tagRequestDto);
    Tag updateTag(TagRequestDto tagRequestDto, Long id);
    void deleteTagById(Long id);
}
