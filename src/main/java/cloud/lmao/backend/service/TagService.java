package cloud.lmao.backend.service;

import cloud.lmao.backend.dto.request.TagRequestDto;
import cloud.lmao.backend.dto.response.TagDto;
import cloud.lmao.backend.repository.Tag;
import cloud.lmao.backend.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public List<TagDto> getAllTags() {
        return tagRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public TagDto createTag(TagRequestDto request) {
        if (tagRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Tag already exists");
        }

        Tag tag = Tag.builder()
                .name(request.getName())
                .build();

        return mapToDto(tagRepository.save(tag));
    }

    public void deleteTag(UUID id) {
        tagRepository.deleteById(id);
    }

    private TagDto mapToDto(Tag tag) {
        return TagDto.builder()
                .id(tag.getId())
                .name(tag.getName())
                .build();
    }
}
