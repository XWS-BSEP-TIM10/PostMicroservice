package com.post.service;

import com.post.dto.NewPostRequestDTO;
import com.post.model.Comment;
import com.post.model.Post;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface PostService {
    Post addPost(NewPostRequestDTO newPostRequestDTO, MultipartFile image) throws IOException;

    List<Post> getPostsFromUser(String id);

    Post addReaction(String postId, String userId, Boolean like);

    Post addComment(String postId, String userId, String text);
}
