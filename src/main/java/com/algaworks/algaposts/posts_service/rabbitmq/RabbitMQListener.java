package com.algaworks.algaposts.posts_service.rabbitmq;

import com.algaworks.algaposts.posts_service.api.model.PostOutput;

import com.algaworks.algaposts.posts_service.common.IdGenerator;
import com.algaworks.algaposts.posts_service.domain.model.Post;
import com.algaworks.algaposts.posts_service.domain.model.PostId;
import com.algaworks.algaposts.posts_service.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;

import static com.algaworks.algaposts.posts_service.rabbitmq.RabbitMQConfig.QUEUE_POST_RESULT;



@Slf4j
@RequiredArgsConstructor
@Component
public class RabbitMQListener {

    private final RabbitTemplate rabbitTemplate;
    private final PostRepository postRepository;

    @SneakyThrows
    @RabbitListener(queues =  QUEUE_POST_RESULT, concurrency = "2-3")
    public void handleProcessingPost(@Payload PostOutput postOutput,
                       @Headers Map<String,Object> headers

    ){
        log.info("POST_SERVICE FILA. Postid {} Author {}", postOutput.getId(), postOutput.getAuthor());

        if((!postOutput.getWordCount().equals(0))&&(!postOutput.getCalculatedValue().equals(0L))){
            log.info("Salvando resultado da fila pos processamento.");
            Post post =  Post.builder()
                    .id( new PostId(postOutput.getId()))
                    .body(postOutput.getBody())
                    .title(postOutput.getTitle())
                    .author(postOutput.getAuthor())
                    .wordCount(postOutput.getWordCount())
                    .calculatedValue(postOutput.getCalculatedValue())
                    .build();

            post = postRepository.saveAndFlush(post);

        }

       Thread.sleep(Duration.ofSeconds(5));
    }

}
