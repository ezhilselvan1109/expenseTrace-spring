package com.expensetrace.app.service.tag;

import com.expensetrace.app.dto.response.tag.TagsResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface TagService {
    Page<TagsResponseDto> getAllTags(Pageable pageable);

    void deleteTag(UUID tagId);

    void mergeTags(UUID sourceTagId, UUID targetTagId);
    TagsResponseDto updateTag(UUID tagId, String name);
}

