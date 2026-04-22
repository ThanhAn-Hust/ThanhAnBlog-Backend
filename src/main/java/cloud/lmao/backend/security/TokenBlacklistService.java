package cloud.lmao.backend.security;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Service quản lý danh sách đen các token (Blacklist) sử dụng Redis.
 * Được dùng khi người dùng đăng xuất (Logout) để vô hiệu hóa token hiện tại.
 */
@Service
public class TokenBlacklistService {

    private final StringRedisTemplate redisTemplate;

    public TokenBlacklistService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Thêm token vào danh sách đen với thời gian sống (TTL).
     * @param token Chuỗi JWT token
     * @param expirationTimeInMills Thời gian sống còn lại của token tính bằng mili giây
     */
    public void addToBlacklist(String token, long expirationTimeInMills) {
        try {
            redisTemplate.opsForValue().set(token, "blacklisted", expirationTimeInMills, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            System.err.println("[WARN] Redis không khả dụng, bỏ qua blacklist token: " + e.getMessage());
        }
    }

    /**
     * Kiểm tra xem token có nằm trong danh sách đen hay không.
     * @param token Chuỗi JWT token
     * @return true nếu token đã bị vô hiệu hóa
     */
    public boolean isBlacklisted(String token) {
        try {
            return Boolean.TRUE.equals(redisTemplate.hasKey(token));
        } catch (Exception e) {
            System.err.println("[WARN] Redis không khả dụng, bỏ qua kiểm tra blacklist: " + e.getMessage());
            return false; // Nếu Redis chết, coi như token hợp lệ
        }
    }
}
