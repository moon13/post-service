package com.algaworks.algaposts.posts_service.api.controller;


import com.algaworks.algaposts.posts_service.api.model.PostInput;
import com.algaworks.algaposts.posts_service.api.model.PostOutput;
import com.algaworks.algaposts.posts_service.common.IdGenerator;
import com.algaworks.algaposts.posts_service.domain.model.Post;
import com.algaworks.algaposts.posts_service.domain.model.PostId;
import com.algaworks.algaposts.posts_service.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostsController {

    //private final SensorMonitoringClient sensorMonitoringClient;
    private final PostRepository postRepository;

    private final RabbitTemplate rabbitTemplate;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostOutput create(@RequestBody PostInput input){
        Post post =  Post.builder()
                .id( new PostId(IdGenerator.generateTSID()))
                .body(input.getBody())
                .title(input.getTitle())
                .author(input.getAuthor())
                .wordCount(0)
                .calculatedValue(Double.valueOf(0L))
                .build();

        post = postRepository.saveAndFlush(post);

        PostOutput postOutput = convertToModel(post);

        String fila =  "text-processor-service.post-processing.v1.q";
        String exchange = "post-processing.post-received.v1.e";
        String routingKey = "";
        Object payload = postOutput;

        rabbitTemplate.convertAndSend(exchange, routingKey, payload);
        //rabbitTemplate.convertAndSend(fila,payload);

        return postOutput;

    }

    private PostOutput convertToModel(Post post) {
        return PostOutput.builder()
                .id(post.getId().getValue())
                .author(post.getAuthor())
                .body(post.getBody())
                .wordCount(post.getWordCount())
                .title(post.getTitle())
                .calculatedValue(post.getCalculatedValue())
                .build();
    }

}
