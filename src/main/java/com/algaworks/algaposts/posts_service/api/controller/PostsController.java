package com.algaworks.algaposts.posts_service.api.controller;


import com.algaworks.algaposts.posts_service.api.model.PostInput;
import com.algaworks.algaposts.posts_service.api.model.PostOutput;
import com.algaworks.algaposts.posts_service.common.IdGenerator;
import com.algaworks.algaposts.posts_service.domain.model.Post;
import com.algaworks.algaposts.posts_service.domain.model.PostId;
import com.algaworks.algaposts.posts_service.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostsController {

    //private final SensorMonitoringClient sensorMonitoringClient;
    private final PostRepository postRepository;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostOutput create(@RequestBody PostInput input){
        Post post =  Post.builder()
                .id( new PostId(IdGenerator.generateTSID()))
                .body(input.getBody())
                .title(input.getTitle())
                .wordCount(Integer.valueOf(input.getBody().trim().length()))
                .calculatedValue(Double.valueOf(0L))
                .build();

        post = postRepository.saveAndFlush(post);

        return convertToModel(post);

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
