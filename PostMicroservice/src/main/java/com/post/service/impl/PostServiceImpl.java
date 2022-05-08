package com.post.service.impl;

import com.post.dto.NewPostRequestDTO;
import com.post.grpc.ConnectionsGrpcClient;
import com.post.model.Comment;
import com.post.model.Post;
import com.post.repository.PostRepository;
import com.post.service.PostService;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository repo;
    private final ConnectionsGrpcClient connectionsGrpcClient;

    public PostServiceImpl(PostRepository repo, ConnectionsGrpcClient connectionsGrpcClient) {
        this.repo = repo;
        this.connectionsGrpcClient = connectionsGrpcClient;
    }

    @Override
    public Post addPost(NewPostRequestDTO newPostRequestDTO, MultipartFile image) throws IOException {
        Post post = new Post(newPostRequestDTO.getOwnerId(),
                newPostRequestDTO.getText(),
                new Binary(BsonBinarySubType.BINARY, image.getBytes()));
        return repo.insert(post);
    }

    @Override
    public Post addPost(NewPostRequestDTO newPostRequestDTO, byte[] image) {
        Post post = new Post(newPostRequestDTO.getOwnerId(),
                newPostRequestDTO.getText(),
                new Binary(BsonBinarySubType.BINARY, image));
        return repo.insert(post);
    }

    @Override
    public List<Post> getPostsFromUser(String id) {
        return repo.findByOwnerId(id, Sort.by(Sort.Direction.DESC, "creationDate"));
    }

    @Override
    public Post addReaction(String postId, String userId, Boolean like) {
        Optional<Post> post = repo.findById(postId);
        if (post.isEmpty() || like == null) return null;
        if (like) return addLike(post.get(), userId);
        return addDislike(post.get(), userId);
    }

    @Override
    public Post addComment(String postId, String userId, String text) {
        Optional<Post> post = repo.findById(postId);
        if (post.isEmpty()) return null;
        Comment newComment = new Comment(userId, text);
        return addComment(post.get(), newComment);
    }

    @Override
    public List<Post> getFeed(String id) {
        List<String> connectionIds = connectionsGrpcClient.getConnections(id);
        return repo.findByOwnerIds(connectionIds, Sort.by(Sort.Direction.DESC, "creationDate"));
    }

    private Post addComment(Post post, Comment newComment) {
        post.getComments().add(newComment);
        return repo.save(post);
    }

    @Override
    public Post removeReaction(String postId, String userId) {
        Optional<Post> post = repo.findById(postId);
        if (post.isEmpty()) return null;
        post.get().getDislikes().removeIf(dislike -> dislike.equals(userId));
        post.get().getLikes().removeIf(dislike -> dislike.equals(userId));
        return repo.save(post.get());
    }

    private Post addLike(Post post, String userId) {
        post.getDislikes().removeIf(dislike -> dislike.equals(userId));
        if (!post.getLikes().contains(userId))
            post.getLikes().add(userId);
        return repo.save(post);
    }

    private Post addDislike(Post post, String userId) {
        post.getLikes().removeIf(dislike -> dislike.equals(userId));
        if (!post.getDislikes().contains(userId))
            post.getDislikes().add(userId);
        return repo.save(post);
    }
}
