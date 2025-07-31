package com.expensetrace.app.controller;

import com.expensetrace.app.dto.request.TagRequestDto;
import com.expensetrace.app.exception.ResourceNotFoundException;
import com.expensetrace.app.response.ApiResponse;
import com.expensetrace.app.dto.response.TagResponseDto;
import com.expensetrace.app.service.tag.ITagService;
import com.expensetrace.app.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/tags")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Tag", description = "Endpoints for managing tags")
public class TagController {
    private final ITagService tagService;
    private final SecurityUtil securityUtil;

    @Operation(
            summary = "Get all tags for the authenticated user",
            description = "Returns a list of all tags created by the currently authenticated user."
    )
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllTags() {
        UUID userId = securityUtil.getAuthenticatedUserId();
        try {
            List<TagResponseDto> tagResponseDto = new ArrayList<>();
            TagResponseDto tagResponse1=new TagResponseDto();
            tagResponse1.setId(UUID.fromString("1e9431a1-adc3-47cb-8645-51b8fd0c6c11"));
            tagResponse1.setTransactions(4);
            tagResponse1.setName("tag1");
            tagResponseDto.add(tagResponse1);

            TagResponseDto tagResponse2=new TagResponseDto();
            tagResponse2.setId(UUID.fromString("1e9431a1-adc3-47cb-8645-51b8fd0c6c22"));
            tagResponse2.setTransactions(5);
            tagResponse2.setName("tag2");
            tagResponseDto.add(tagResponse2);

            TagResponseDto tagResponse3=new TagResponseDto();
            tagResponse3.setId(UUID.fromString("1e9431a1-adc3-47cb-8645-51b8fd0c6c33"));
            tagResponse3.setTransactions(3);
            tagResponse3.setName("tag3");
            tagResponseDto.add(tagResponse3);

            TagResponseDto tagResponse4=new TagResponseDto();
            tagResponse4.setId(UUID.fromString("1e9431a1-adc3-47cb-8645-51b8fd0c6c44"));
            tagResponse4.setTransactions(4);
            tagResponse4.setName("tag4");
            tagResponseDto.add(tagResponse4);

            TagResponseDto tagResponse5=new TagResponseDto();
            tagResponse5.setId(UUID.fromString("1e9431a1-adc3-47cb-8645-51b8fd0c6c55"));
            tagResponse5.setTransactions(5);
            tagResponse5.setName("tag5");
            tagResponseDto.add(tagResponse5);

            TagResponseDto tagResponse6=new TagResponseDto();
            tagResponse6.setId(UUID.fromString("1e9431a1-adc3-47cb-8645-51b8fd0c6c66"));
            tagResponse6.setTransactions(3);
            tagResponse6.setName("tag6");
            tagResponseDto.add(tagResponse6);

            return ResponseEntity.ok(new ApiResponse("Found!", tagResponseDto));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error:", INTERNAL_SERVER_ERROR));
        }
    }

    @Operation(
            summary = "Delete a tag by ID",
            description = "Deletes the tag with the specified ID. Throws an error if the tag is not found."
    )
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<ApiResponse> deleteTag(@PathVariable UUID id) {
        try {
            //tagService.deleteTagById(id);
            return ResponseEntity.ok(new ApiResponse("Deleted successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @Operation(
            summary = "merge a tag by ID",
            description = "Merges the tag with the specified ID into another tag identified by tagId parameter. Throws an error if either tag is not found."
    )
    @PutMapping("/{id}/merge")
    public ResponseEntity<ApiResponse> mergeTag(@PathVariable UUID id, @RequestParam UUID tagId) {
        try {
            return ResponseEntity.ok(new ApiResponse("Deleted successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @Operation(
            summary = "Update a tag by ID",
            description = "Updates the tag with the specified ID using the provided details. Throws an error if the tag is not found."
    )
    @PutMapping("/{id}/update")
    public ResponseEntity<ApiResponse> updateTag(@PathVariable UUID id, @Valid @RequestBody TagRequestDto tag) {
        try {
            //TagResponseDto updatedTagResponseDto = tagService.updateTag(tag, id);
            return ResponseEntity.ok(new ApiResponse("Update success!", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
