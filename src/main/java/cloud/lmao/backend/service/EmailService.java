package cloud.lmao.backend.service;

import cloud.lmao.backend.repository.Subscriber;
import cloud.lmao.backend.repository.SubscriberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final SubscriberRepository subscriberRepository;

    @Async
    @EventListener
    public void handlePostPublishedEvent(PostPublishedEvent event) {
        log.info("Bắt đầu tiến trình gửi Email cho bài viết: {}", event.getPost().getTitle());

        List<Subscriber> subscribers = subscriberRepository.findAllByIsActiveTrue();
        if (subscribers.isEmpty()) {
            log.info("Không có Subscriber nào để gửi.");
            return;
        }

        String subject = "Thành An vừa đăng bài viết mới: " + event.getPost().getTitle();
        String text = "Chào bạn,\n\nTôi vừa đăng một bài viết mới trên Blog: " + event.getPost().getTitle() 
                + "\nBạn có thể đọc nó tại: https://blog.thanhanlv.cloud/posts/" + event.getPost().getSlug()
                + "\n\nCảm ơn bạn đã theo dõi!";

        for (Subscriber sub : subscribers) {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(sub.getEmail());
                message.setSubject(subject);
                message.setText(text);
                message.setFrom("no-reply@thanhanlv.cloud");
                
                mailSender.send(message);
                log.info("Đã gửi email thành công tới: {}", sub.getEmail());
            } catch (Exception e) {
                log.error("Lỗi khi gửi email tới {}: {}", sub.getEmail(), e.getMessage());
            }
        }
    }
}
