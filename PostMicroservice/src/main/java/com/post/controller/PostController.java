package com.post.controller;

import com.post.dto.*;
import com.post.grpc.ConnectionsGrpcClient;
import com.post.model.Post;
import com.post.service.PostService;
import org.springframework.http.HttpStatus;
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
    private final ConnectionsGrpcClient connectionsGrpcClient;

    public PostController(PostService postService, ConnectionsGrpcClient connectionsGrpcClient) {
        this.postService = postService;
        this.connectionsGrpcClient = connectionsGrpcClient;
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

    @PutMapping(value = "users/{userId}/posts/{postId}/reaction")
    public ResponseEntity<HttpStatus> addReaction(@PathVariable String userId, @PathVariable String postId, @RequestBody ReactionDTO dto){
        Post post = postService.addReaction(postId, userId, dto.isLike());
        if(post == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "users/{userId}/posts/{postId}/comment")
    public ResponseEntity<PostsResponseDTO> commentPost(@PathVariable String userId, @PathVariable String postId, @RequestBody NewCommentDTO dto) {
        Post commentedPost = postService.addComment(postId, userId, dto.getText());
        if(commentedPost == null) return ResponseEntity.badRequest().build();
        PostsResponseDTO postsResponseDTO = new PostsResponseDTO(commentedPost);
        postsResponseDTO.setImage(Base64.getEncoder().encodeToString(commentedPost.getImage().getData()));
        return ResponseEntity.ok(postsResponseDTO);
    }

    @GetMapping(value="user/{uuid}/feed")
    public ResponseEntity<ConnectionsDto> userFeed(@PathVariable String uuid) {

        return ResponseEntity.ok(new ConnectionsDto(connectionsGrpcClient.getConnections(uuid)));

    }
}
