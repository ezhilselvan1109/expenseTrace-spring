package com.expensetrace.app.controller;

import com.expensetrace.app.requestDto.TagRequestDto;
import com.expensetrace.app.exception.AlreadyExistsException;
import com.expensetrace.app.exception.ResourceNotFoundException;
import com.expensetrace.app.model.Tag;
import com.expensetrace.app.response.ApiResponse;
import com.expensetrace.app.service.tag.ITagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/tags")
@io.swagger.v3.oas.annotations.tags.Tag(name = "TagController", description = "Manage your TagController")
public class TagController {
    private final ITagService tagService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllTags() {
        try {
            List<Tag> tag = tagService.getAllTags();
            return  ResponseEntity.ok(new ApiResponse("Found!", tag));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error:", INTERNAL_SERVER_ERROR));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addTag(@RequestBody TagRequestDto name) {
        try {
            Tag theTag = tagService.addTag(name);
            return  ResponseEntity.ok(new ApiResponse("Success", theTag));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/tag/{id}/tag")
    public ResponseEntity<ApiResponse> getTagById(@PathVariable Long id){
        try {
            Tag theTag = tagService.getTagById(id);
            return  ResponseEntity.ok(new ApiResponse("Found", theTag));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/tag/{name}/tag")
    public ResponseEntity<ApiResponse> getTagByName(@PathVariable String name){
        try {
            Tag theTag = tagService.getTagByName(name);
            return  ResponseEntity.ok(new ApiResponse("Found", theTag));
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
            Tag updatedTag = tagService.updateTag(tag, id);
            return ResponseEntity.ok(new ApiResponse("Update success!", updatedTag));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
