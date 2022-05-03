package com.post.repository;

import com.post.model.Post;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {

    List<Post> findByOwnerId(String ownerId, Sort sort);

    @Query("{ ownerId: { $in: ?0 } }")
    List<Post> findByOwnerIds(List<String> ownerIds, Sort sort);
}
