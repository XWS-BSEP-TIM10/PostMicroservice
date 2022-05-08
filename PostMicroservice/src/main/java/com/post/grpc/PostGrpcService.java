package com.post.grpc;

import com.post.dto.NewPostRequestDTO;
import com.post.model.Comment;
import com.post.model.Post;
import com.post.service.PostService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import proto.AddPostProto;
import proto.AddPostResponseProto;
import proto.AddReactionProto;
import proto.AddReactionResponseProto;
import proto.CommentPostProto;
import proto.CommentPostResponseProto;
import proto.CommentProto;
import proto.PostGrpcServiceGrpc;
import proto.PostProto;
import proto.RemoveReactionProto;
import proto.RemoveReactionResponseProto;
import proto.UserPostsProto;
import proto.UserPostsResponseProto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@GrpcService
public class PostGrpcService extends PostGrpcServiceGrpc.PostGrpcServiceImplBase {

    private final PostService postService;
    private static final SimpleDateFormat iso8601Formater = new SimpleDateFormat("dd/MM/yyyy");

    @Autowired
    public PostGrpcService(PostService postService) {
        this.postService = postService;
    }

    @Override
    public void addReaction(AddReactionProto request, StreamObserver<AddReactionResponseProto> responseObserver) {

        Post post = postService.addReaction(request.getPostId(), request.getUserId(), request.getLike());
        AddReactionResponseProto addReactionResponseProto;

        if (post == null)
            addReactionResponseProto = AddReactionResponseProto.newBuilder().setStatus("Status 400").build();
        else
            addReactionResponseProto = AddReactionResponseProto.newBuilder().setStatus("Status 200").build();

        responseObserver.onNext(addReactionResponseProto);
        responseObserver.onCompleted();

    }

    @Override
    public void removeReaction(RemoveReactionProto request, StreamObserver<RemoveReactionResponseProto> responseObserver) {

        Post post = postService.removeReaction(request.getPostId(), request.getUserId());
        RemoveReactionResponseProto responseProto;

        if (post == null)
            responseProto = RemoveReactionResponseProto.newBuilder().setStatus("Status 404").build();
        else
            responseProto = RemoveReactionResponseProto.newBuilder().setStatus("Status 200").build();

        responseObserver.onNext(responseProto);
        responseObserver.onCompleted();

    }

    @Override
    public void addPost(AddPostProto request, StreamObserver<AddPostResponseProto> responseObserver) {
        NewPostRequestDTO newPostRequestDTO = new NewPostRequestDTO(request.getOwnerId(), request.getText());
        Post newPost = postService.addPost(newPostRequestDTO, request.getImage().toByteArray());
        AddPostResponseProto addPostResponseProto = AddPostResponseProto.newBuilder()
                .setId(newPost.getId())
                .setStatus("Status 200")
                .build();

        responseObserver.onNext(addPostResponseProto);
        responseObserver.onCompleted();
    }

    @Override
    public void commentPost(CommentPostProto request, StreamObserver<CommentPostResponseProto> responseObserver) {

        Post post = postService.addComment(request.getPostId(), request.getUserId(), request.getComment());
        CommentPostResponseProto commentPostResponseProto;

        if (post == null)
            commentPostResponseProto = CommentPostResponseProto.newBuilder().setComment(request.getComment()).setStatus("Status 400").build();
        else
            commentPostResponseProto = CommentPostResponseProto.newBuilder().setComment(request.getComment()).setStatus("Status 200").build();

        responseObserver.onNext(commentPostResponseProto);
        responseObserver.onCompleted();

    }

    @Override
    public void getUserPosts(UserPostsProto request, StreamObserver<UserPostsResponseProto> responseObserver) {
        List<Post> posts = postService.getPostsFromUser(request.getUserId());
        List<PostProto> postsProto = new ArrayList<>();
        for (Post post : posts) {
            List<CommentProto> commentsProto = new ArrayList<>();
            for (Comment c : post.getComments()) {
                commentsProto.add(CommentProto.newBuilder().setUserId(c.getUserId()).setText(c.getText()).build());
            }
            PostProto postProto = PostProto.newBuilder()
                    .setPostId(post.getId())
                    .setText(post.getText())
                    .setOwnerId(post.getOwnerId())
                    .setCreationDate(iso8601Formater.format(post.getCreationDate()))
                    .setImage(Base64.getEncoder().encodeToString(post.getImage().getData()))
                    .addAllLikes(post.getLikes())
                    .addAllComments(commentsProto)
                    .addAllDislikes(post.getDislikes())
                    .build();
            postsProto.add(postProto);
        }
        UserPostsResponseProto userPostsResponseProto = UserPostsResponseProto.newBuilder()
                .addAllPosts(postsProto)
                .build();

        responseObserver.onNext(userPostsResponseProto);
        responseObserver.onCompleted();
    }

    @Override
    public void getUserFeed(UserPostsProto request, StreamObserver<UserPostsResponseProto> responseObserver) {
        List<Post> posts = postService.getFeed(request.getUserId());
        List<PostProto> postsProto = new ArrayList<>();
        for (Post post : posts) {
            List<CommentProto> commentsProto = new ArrayList<>();
            for (Comment c : post.getComments()) {
                commentsProto.add(CommentProto.newBuilder().setUserId(c.getUserId()).setText(c.getText()).build());
            }
            PostProto postProto = PostProto.newBuilder()
                    .setPostId(post.getId())
                    .setText(post.getText())
                    .setOwnerId(post.getOwnerId())
                    .setCreationDate(iso8601Formater.format(post.getCreationDate()))
                    .setImage(Base64.getEncoder().encodeToString(post.getImage().getData()))
                    .addAllLikes(post.getLikes())
                    .addAllComments(commentsProto)
                    .addAllDislikes(post.getDislikes())
                    .build();
            postsProto.add(postProto);
        }
        UserPostsResponseProto userPostsResponseProto = UserPostsResponseProto.newBuilder()
                .addAllPosts(postsProto)
                .build();

        responseObserver.onNext(userPostsResponseProto);
        responseObserver.onCompleted();
    }
}
