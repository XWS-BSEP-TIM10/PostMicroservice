package com.post.controller;

import com.post.dto.*;
import com.post.model.Comment;
import com.post.model.Post;
import com.post.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @PutMapping(value = "posts/reaction")
    public ResponseEntity<PostsResponseDTO> addReaction(@RequestBody ReactionDTO dto){
        Post post = postService.addReaction(dto.getPostId(), dto.getUserId(), dto.isLike());
        PostsResponseDTO postsResponseDTO = new PostsResponseDTO(post);
        postsResponseDTO.setImage(Base64.getEncoder().encodeToString(post.getImage().getData()));
        return ResponseEntity.ok(postsResponseDTO);
    }

    @PutMapping(value = "posts/comment")
    public ResponseEntity<PostsResponseDTO> commentPost(@RequestBody NewCommentDTO dto) {
        Post commentedPost = postService.addComment(dto.getPostId(), dto.getUserId(), dto.getText());
        if(commentedPost == null) return ResponseEntity.badRequest().build();
        PostsResponseDTO postsResponseDTO = new PostsResponseDTO(commentedPost);
        postsResponseDTO.setImage(Base64.getEncoder().encodeToString(commentedPost.getImage().getData()));
        return ResponseEntity.ok(postsResponseDTO);
    }

    @GetMapping(value="user/{id}/feed")
    public void userFeed(@PathVariable String id) {


    }
}
