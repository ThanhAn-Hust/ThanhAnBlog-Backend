package cloud.lmao.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    
    Optional<Post> findBySlug(String slug);
    
    boolean existsBySlug(String slug);

    // Lọc theo trạng thái và phân trang (Cho trang Blog)
    Page<Post> findAllByStatusOrderByPublishedAtDesc(String status, Pageable pageable);

    // Lọc theo trạng thái và Tag
    @Query("SELECT p FROM Post p JOIN p.tags t WHERE p.status = :status AND t.id = :tagId ORDER BY p.publishedAt DESC")
    Page<Post> findByStatusAndTagId(@Param("status") String status, @Param("tagId") UUID tagId, Pageable pageable);
}
