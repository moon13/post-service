package com.algaworks.algaposts.posts_service.domain.model;

import io.hypersistence.tsid.TSID;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Post {
    @Id
    @AttributeOverride( name = "value", column = @Column(name = "id", columnDefinition = "BIGINT"))
    private PostId id;
    private String title;
    private String body;
    private String author;
    private Integer wordCount;
    private Double calculatedValue;


}
