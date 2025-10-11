package com.algaworks.algaposts.posts_service.api.model;

import io.hypersistence.tsid.TSID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostOutput {

    private TSID id;
    private String title;
    private String body;
    private String author;
    private Integer wordCount;
    private Double calculatedValue;

}
