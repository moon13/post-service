package com.algaworks.algaposts.posts_service.domain.repository;

import com.algaworks.algaposts.posts_service.domain.model.Post;
import com.algaworks.algaposts.posts_service.domain.model.PostId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, PostId> {
}
