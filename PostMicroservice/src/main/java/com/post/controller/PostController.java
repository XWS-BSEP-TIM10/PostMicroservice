package com.post.controller;

import com.post.dto.NewPostRequestDTO;
import com.post.dto.NewPostResponseDTO;
import com.post.model.Post;
import com.post.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping(value = "/api/v1/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping()
    public ResponseEntity<NewPostResponseDTO> addPost(@RequestPart("post") @Valid NewPostRequestDTO newPostRequestDTO,
                                                      @RequestPart("image") MultipartFile image) throws IOException {
        Post newPost = postService.addPost(newPostRequestDTO, image);
        NewPostResponseDTO newPostResponseDTO = new NewPostResponseDTO(newPost.getId());
        return ResponseEntity.ok(newPostResponseDTO);
    }
}
