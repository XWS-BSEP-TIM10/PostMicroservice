package com.post.service;

import com.post.dto.NewPostRequestDTO;
import com.post.model.Post;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface PostService {
    Post addPost(NewPostRequestDTO newPostRequestDTO, MultipartFile image) throws IOException;
}
