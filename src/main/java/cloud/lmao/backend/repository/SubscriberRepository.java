package cloud.lmao.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubscriberRepository extends JpaRepository<Subscriber, UUID> {
    Optional<Subscriber> findByEmail(String email);
    boolean existsByEmail(String email);
    
    // Lấy tất cả user đang đăng ký (isActive = true)
    List<Subscriber> findAllByIsActiveTrue();
}
