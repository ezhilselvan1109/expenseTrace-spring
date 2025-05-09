package com.expensetrace.app.controller;

import com.expensetrace.app.requestDto.TagRequestDto;
import com.expensetrace.app.exception.AlreadyExistsException;
import com.expensetrace.app.exception.ResourceNotFoundException;
import com.expensetrace.app.response.ApiResponse;
import com.expensetrace.app.responseDto.TagResponseDto;
import com.expensetrace.app.service.tag.ITagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/tags")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Tag", description = "Manage your TagController")
public class TagController {
    private final ITagService tagService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllTags() {
        try {
            List<TagResponseDto> tagResponseDto = tagService.getAllTags();
            return  ResponseEntity.ok(new ApiResponse("Found!", tagResponseDto));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error:", INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("user/all")
    public ResponseEntity<ApiResponse> getAllTagsByUser() {
        Long userId=1L;
        try {
            List<TagResponseDto> tagResponseDto = tagService.getAllTagsByUser(userId);
            return  ResponseEntity.ok(new ApiResponse("Found!", tagResponseDto));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error:", INTERNAL_SERVER_ERROR));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addTag(@RequestBody TagRequestDto tagRequestDto) {
        Long userId=1L;
        try {
            TagResponseDto theTagResponseDto = tagService.addTag(tagRequestDto,userId);
            return  ResponseEntity.ok(new ApiResponse("Success", theTagResponseDto));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }



    @GetMapping("/tag/{id}/tag")
    public ResponseEntity<ApiResponse> getTagById(@PathVariable Long id){
        try {
            TagResponseDto theTagResponseDto = tagService.getTagById(id);
            return  ResponseEntity.ok(new ApiResponse("Found", theTagResponseDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/tag/{name}/tag")
    public ResponseEntity<ApiResponse> getTagByName(@PathVariable String name){
        try {
            TagResponseDto theTagResponseDto = tagService.getTagByName(name);
            return  ResponseEntity.ok(new ApiResponse("Found", theTagResponseDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }


    @DeleteMapping("/tag/{id}/delete")
    public ResponseEntity<ApiResponse> deleteTag(@PathVariable Long id){
        try {
            tagService.deleteTagById(id);
            return  ResponseEntity.ok(new ApiResponse("Found", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/tag/{id}/update")
    public ResponseEntity<ApiResponse> updateTag(@PathVariable Long id, @RequestBody TagRequestDto tag) {
        try {
            TagResponseDto updatedTagResponseDto = tagService.updateTag(tag, id);
            return ResponseEntity.ok(new ApiResponse("Update success!", updatedTagResponseDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
