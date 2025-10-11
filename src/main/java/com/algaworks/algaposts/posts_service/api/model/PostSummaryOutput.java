package com.algaworks.algaposts.posts_service.api.model;

import io.hypersistence.tsid.TSID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostSummaryOutput {
    private TSID id;
    private String title;
    private String summary;
    private String author;


}
