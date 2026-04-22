package cloud.lmao.backend.service;

import cloud.lmao.backend.dto.request.PostRequestDto;
import cloud.lmao.backend.dto.response.CategoryDto;
import cloud.lmao.backend.dto.response.PostResponseDto;
import cloud.lmao.backend.dto.response.TagDto;
import cloud.lmao.backend.repository.Category;
import cloud.lmao.backend.repository.CategoryRepository;
import cloud.lmao.backend.repository.Post;
import cloud.lmao.backend.repository.PostRepository;
import cloud.lmao.backend.repository.Tag;
import cloud.lmao.backend.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional(readOnly = true)
    public Page<PostResponseDto> getPublicPosts(UUID tagId, Pageable pageable) {
        Page<Post> posts;
        if (tagId != null) {
            posts = postRepository.findByStatusAndTagId("PUBLISHED", tagId, pageable);
        } else {
            posts = postRepository.findAllByStatusOrderByPublishedAtDesc("PUBLISHED", pageable);
        }
        return posts.map(this::mapToDto);
    }

    @Transactional
    public PostResponseDto getPostBySlug(String slug) {
        Post post = postRepository.findBySlug(slug)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        
        post.setViewCount(post.getViewCount() + 1);
        postRepository.save(post);
        return mapToDto(post);
    }

    @Transactional
    public PostResponseDto createPost(PostRequestDto request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        List<Tag> tags = tagRepository.findAllById(request.getTagIds());

        String slug = generateSlug(request.getTitle());
        if (postRepository.existsBySlug(slug)) {
            slug = slug + "-" + UUID.randomUUID().toString().substring(0, 5);
        }

        Post post = Post.builder()
                .title(request.getTitle())
                .slug(slug)
                .content(request.getContent())
                .summary(request.getSummary())
                .thumbnail(request.getThumbnail())
                .status(request.getStatus())
                .category(category)
                .tags(new HashSet<>(tags))
                .viewCount(0)
                .build();

        if ("PUBLISHED".equals(request.getStatus())) {
            post.setPublishedAt(LocalDateTime.now());
        }

        Post savedPost = postRepository.save(post);

        // Phát sự kiện gửi mail nếu được chọn và bài được xuất bản
        if ("PUBLISHED".equals(request.getStatus()) && request.isNotifySubscribers()) {
            eventPublisher.publishEvent(new PostPublishedEvent(this, savedPost));
        }

        return mapToDto(savedPost);
    }

    @Transactional
    public void deletePost(UUID id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        // Đổi trạng thái sang ARCHIVED thay vì xóa cứng
        post.setStatus("ARCHIVED");
        postRepository.save(post);
    }

    private PostResponseDto mapToDto(Post post) {
        CategoryDto categoryDto = null;
        if (post.getCategory() != null) {
            categoryDto = CategoryDto.builder()
                    .id(post.getCategory().getId())
                    .name(post.getCategory().getName())
                    .slug(post.getCategory().getSlug())
                    .build();
        }

        List<TagDto> tagDtos = post.getTags().stream()
                .map(t -> TagDto.builder().id(t.getId()).name(t.getName()).build())
                .collect(Collectors.toList());

        return PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .slug(post.getSlug())
                .content(post.getContent())
                .summary(post.getSummary())
                .thumbnail(post.getThumbnail())
                .status(post.getStatus())
                .viewCount(post.getViewCount())
                .category(categoryDto)
                .tags(tagDtos)
                .publishedAt(post.getPublishedAt())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    private String generateSlug(String input) {
        return input.toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("^-|-$", "");
    }
}
