package cloud.lmao.backend.service;

import cloud.lmao.backend.dto.request.SubscribeRequestDto;
import cloud.lmao.backend.repository.Subscriber;
import cloud.lmao.backend.repository.SubscriberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscriberService {

    private final SubscriberRepository subscriberRepository;

    public void subscribe(SubscribeRequestDto request) {
        if (subscriberRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already subscribed");
        }

        Subscriber subscriber = Subscriber.builder()
                .email(request.getEmail())
                .isActive(true)
                .build();

        subscriberRepository.save(subscriber);
    }
}
