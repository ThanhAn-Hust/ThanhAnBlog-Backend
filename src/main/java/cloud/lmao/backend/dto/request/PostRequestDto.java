package cloud.lmao.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestDto {
    
    @NotBlank(message = "Title is required")
    private String title;
    
    @NotBlank(message = "Content is required")
    private String content;
    
    private String summary;
    private String thumbnail;
    
    @NotBlank(message = "Status is required (DRAFT, PUBLISHED, ARCHIVED)")
    private String status;
    
    private UUID categoryId;
    
    private List<UUID> tagIds;

    private List<String> tagNames;
    
    // Flag để gửi mail khi đăng bài
    @Builder.Default
    private boolean notifySubscribers = false;
}
