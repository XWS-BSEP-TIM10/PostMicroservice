package com.post.repository;

import com.post.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {

    List<Post> getByOwnerId(String ownerId);
}
