package cloud.lmao.backend.service;

import cloud.lmao.backend.repository.Post;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PostPublishedEvent extends ApplicationEvent {
    
    private final Post post;

    public PostPublishedEvent(Object source, Post post) {
        super(source);
        this.post = post;
    }
}
