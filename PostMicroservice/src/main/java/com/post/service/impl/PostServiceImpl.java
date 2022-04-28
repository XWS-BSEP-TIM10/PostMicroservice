package com.post.service.impl;

import com.post.dto.NewPostRequestDTO;
import com.post.model.Post;
import com.post.repository.PostRepository;
import com.post.service.PostService;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository repo;

    public PostServiceImpl(PostRepository repo) {
        this.repo = repo;
    }


    @Override
    public Post addPost(NewPostRequestDTO newPostRequestDTO, MultipartFile image) throws IOException {
        Post post = new Post(newPostRequestDTO.getOwnerId(),
                newPostRequestDTO.getText(),
                new Binary(BsonBinarySubType.BINARY, image.getBytes()));
        return repo.insert(post);

    }

    @Override
    public List<Post> getPostsFromUser(String id) {
        return repo.getByOwnerId(id);
    }
}
