package cloud.lmao.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {
    private UUID id;
    private String title;
    private String slug;
    private String content;
    private String summary;
    private String thumbnail;
    private String status;
    private Integer viewCount;
    private CategoryDto category;
    private List<TagDto> tags;
    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
