package cloud.lmao.backend.service;

import cloud.lmao.backend.dto.request.CategoryRequestDto;
import cloud.lmao.backend.dto.response.CategoryDto;
import cloud.lmao.backend.repository.Category;
import cloud.lmao.backend.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public CategoryDto createCategory(CategoryRequestDto request) {
        String slug = generateSlug(request.getName());
        if (categoryRepository.existsBySlug(slug)) {
            throw new IllegalArgumentException("Category with this name already exists");
        }

        Category category = Category.builder()
                .name(request.getName())
                .slug(slug)
                .build();

        return mapToDto(categoryRepository.save(category));
    }

    public void deleteCategory(UUID id) {
        categoryRepository.deleteById(id);
    }

    private CategoryDto mapToDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .slug(category.getSlug())
                .build();
    }

    private String generateSlug(String input) {
        return input.toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("^-|-$", "");
    }
}
