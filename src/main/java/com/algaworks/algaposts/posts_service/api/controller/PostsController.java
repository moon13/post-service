package com.algaworks.algaposts.posts_service.api.controller;


import com.algaworks.algaposts.posts_service.api.model.PostInput;
import com.algaworks.algaposts.posts_service.api.model.PostOutput;
import com.algaworks.algaposts.posts_service.common.IdGenerator;
import com.algaworks.algaposts.posts_service.domain.model.Post;
import com.algaworks.algaposts.posts_service.domain.model.PostId;
import com.algaworks.algaposts.posts_service.domain.repository.PostRepository;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.algaworks.algaposts.posts_service.rabbitmq.RabbitMQConfig.QUEUE_POST_PROCESSING;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostsController {
 
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

        String exchange = "";
        String routingKey = QUEUE_POST_PROCESSING;
        Object payload = postOutput;

        rabbitTemplate.convertAndSend(exchange, routingKey, payload);

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

    @GetMapping("{postId}")
    public PostOutput getDetail(@PathVariable TSID postId){
        Post postMonitoring = findByIdOrDefault(postId);


        return PostOutput.builder()
                .id(postMonitoring.getId().getValue())
                .body(postMonitoring.getBody())
                .title(postMonitoring.getTitle())
                .author(postMonitoring.getAuthor())
                .wordCount(postMonitoring.getWordCount())
                .calculatedValue(postMonitoring.getCalculatedValue())
                .build();
    }

    private Post findByIdOrDefault(TSID postId) {
        return postRepository.findById(new PostId(postId))
                .orElse(Post.builder()
                        .id(new PostId(postId))
                        .body("")
                        .author("")
                        .title("")
                        .wordCount(0)
                        .calculatedValue(Double.valueOf("0"))
                        .build());
    }

}
