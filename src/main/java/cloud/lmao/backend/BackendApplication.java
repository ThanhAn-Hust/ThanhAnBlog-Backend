package cloud.lmao.backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;

import cloud.lmao.backend.repository.User;
import cloud.lmao.backend.repository.UserRepository;

import java.util.UUID;

@SpringBootApplication
@EnableAsync
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Bean
	public CommandLineRunner initAdminUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			if (userRepository.findByUsername("ancoadmin").isEmpty()) {
				User admin = User.builder()
						.id(UUID.randomUUID())
						.username("ancoadmin")
						.password(passwordEncoder.encode("thanhansblogonhighnine"))
						.email("thanhanlevan@gmail.com")
						.role("ROLE_ADMIN")
						.build();
				userRepository.save(admin);
				System.out.println("Tạo thành công tài khoản Admin mặc định");
			}
		};
	}
}
