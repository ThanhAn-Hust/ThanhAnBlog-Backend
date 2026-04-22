package cloud.lmao.backend.controller;

import cloud.lmao.backend.dto.request.SubscribeRequestDto;
import cloud.lmao.backend.dto.response.CategoryDto;
import cloud.lmao.backend.dto.response.PostResponseDto;
import cloud.lmao.backend.dto.response.TagDto;
import cloud.lmao.backend.service.CategoryService;
import cloud.lmao.backend.service.PostService;
import cloud.lmao.backend.service.SubscriberService;
import cloud.lmao.backend.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/public")
@RequiredArgsConstructor
public class PublicController {

    private final PostService postService;
    private final CategoryService categoryService;
    private final TagService tagService;
    private final SubscriberService subscriberService;

    // --- Lấy danh sách Bài viết (Có phân trang và lọc) ---
    @GetMapping("/posts")
    public ResponseEntity<Page<PostResponseDto>> getPosts(
            @RequestParam(required = false) UUID tagId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(postService.getPublicPosts(tagId, pageable));
    }

    // --- Xem chi tiết Bài viết bằng Slug ---
    @GetMapping("/posts/{slug}")
    public ResponseEntity<PostResponseDto> getPostBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(postService.getPostBySlug(slug));
    }

    // --- Danh sách Category & Tag ---
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDto>> getCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/tags")
    public ResponseEntity<List<TagDto>> getTags() {
        return ResponseEntity.ok(tagService.getAllTags());
    }

    // --- Tính năng Đăng ký nhận bản tin ---
    @PostMapping("/subscribe")
    public ResponseEntity<String> subscribe(@Valid @RequestBody SubscribeRequestDto request) {
        subscriberService.subscribe(request);
        return ResponseEntity.ok("Subscribed successfully!");
    }
}
