package cloud.lmao.backend.controller;

import cloud.lmao.backend.dto.request.CategoryRequestDto;
import cloud.lmao.backend.dto.request.PostRequestDto;
import cloud.lmao.backend.dto.request.TagRequestDto;
import cloud.lmao.backend.dto.response.CategoryDto;
import cloud.lmao.backend.dto.response.PostResponseDto;
import cloud.lmao.backend.dto.response.TagDto;
import cloud.lmao.backend.service.CategoryService;
import cloud.lmao.backend.service.PostService;
import cloud.lmao.backend.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final PostService postService;
    private final CategoryService categoryService;
    private final TagService tagService;

    // --- POSTS ---
    @PostMapping("/posts")
    public ResponseEntity<PostResponseDto> createPost(@Valid @RequestBody PostRequestDto request) {
        return new ResponseEntity<>(postService.createPost(request), HttpStatus.CREATED);
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable UUID id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    // --- CATEGORIES ---
    @PostMapping("/categories")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryRequestDto request) {
        return new ResponseEntity<>(categoryService.createCategory(request), HttpStatus.CREATED);
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    // --- TAGS ---
    @PostMapping("/tags")
    public ResponseEntity<TagDto> createTag(@Valid @RequestBody TagRequestDto request) {
        return new ResponseEntity<>(tagService.createTag(request), HttpStatus.CREATED);
    }

    @DeleteMapping("/tags/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable UUID id) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }
}
