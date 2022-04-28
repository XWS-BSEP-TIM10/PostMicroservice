package com.post.controller;

import com.post.dto.NewPostRequestDTO;
import com.post.dto.NewPostResponseDTO;
import com.post.dto.PostsResponseDTO;
import com.post.model.Post;
import com.post.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping(value = "posts")
    public ResponseEntity<NewPostResponseDTO> addPost(@RequestPart("post") @Valid NewPostRequestDTO newPostRequestDTO,
                                                      @RequestPart("image") MultipartFile image) throws IOException {
        Post newPost = postService.addPost(newPostRequestDTO, image);
        NewPostResponseDTO newPostResponseDTO = new NewPostResponseDTO(newPost.getId());
        return ResponseEntity.ok(newPostResponseDTO);
    }


    @GetMapping(value = "users/{id}/posts")
    public ResponseEntity<List<PostsResponseDTO>> getUserPosts(@PathVariable String id) {
        List<Post> posts = postService.getPostsFromUser(id);
        List<PostsResponseDTO> responseDTOS = new ArrayList<>();
        for (Post post : posts) {
            PostsResponseDTO postsResponseDTO = new PostsResponseDTO(post);
            postsResponseDTO.setImage(Base64.getEncoder().encodeToString(post.getImage().getData()));
            responseDTOS.add(postsResponseDTO);
        }
        return ResponseEntity.ok(responseDTOS);
    }

}
